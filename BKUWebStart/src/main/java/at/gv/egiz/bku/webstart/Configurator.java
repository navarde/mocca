/*
 * Copyright 2008 Federal Chancellery Austria and
 * Graz University of Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.gv.egiz.bku.webstart;

import at.gv.egiz.bku.utils.StreamUtil;
import iaik.asn1.CodingException;
import iaik.xml.crypto.utils.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.dom.ThisExpression;

/**
 *
 * @author Clemens Orthacker <clemens.orthacker@iaik.tugraz.at>
 */
public class Configurator {

  /**
   * MOCCA configuration
   * configurations with less than this (major) version will be backuped and updated
   * allowed: MAJOR[.MINOR[.X[-SNAPSHOT]]]
   */
  public static final String MIN_CONFIG_VERSION = "1.0.9-SNAPSHOT";
  public static final String CONFIG_DIR = ".mocca/conf/";
  public static final String CERTS_DIR = ".mocca/certs/";
  public static final String VERSION_FILE = ".version";
  public static final String UNKOWN_VERSION = "unknown";
  public static final String CONF_TEMPLATE_FILE = "conf-tmp.zip";
  public static final String CONF_TEMPLATE_RESOURCE = "at/gv/egiz/bku/webstart/conf/conf.zip";
  public static final String CERTIFICATES_PKG = "at/gv/egiz/bku/certs";

  /**
   * MOCCA TLS certificate
   */
  public static final String KEYSTORE_FILE = "keystore.ks";
  public static final String PASSWD_FILE = ".secret";

  private static final Log log = LogFactory.getLog(Configurator.class);
  
  /** currently installed configuration version */
  private String version;
  private String certsVersion;
  /** whether a new MOCCA TLS cert was created during initialization */
  private boolean certRenewed = false;

  /**
   * Checks whether the config directory already exists and creates it otherwise.
   * @param configDir the config directory to be created
   * @throws IOException config/certificate creation failed
   * @throws GeneralSecurityException if MOCCA TLS certificate could not be created
   * @throws CodingException if MOCCA TLS certificate could not be created
   */
  public void ensureConfiguration() throws IOException, CodingException, GeneralSecurityException {
    File configDir = new File(System.getProperty("user.home") + '/' + CONFIG_DIR);
    if (configDir.exists()) {
      if (configDir.isFile()) {
        log.error("invalid config directory: " + configDir);
        throw new IOException("invalid config directory: " + configDir);
      } else {
        version = readVersion(new File(configDir, VERSION_FILE));
        if (log.isDebugEnabled()) {
          log.debug("config directory " + configDir + ", version " + version);
        }
        if (updateRequired(version)) {
          File moccaDir = configDir.getParentFile();
          File zipFile = new File(moccaDir, "conf-" + version + ".zip");
          ZipOutputStream zipOS = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
          log.info("backup configuration to " + zipFile);
          backupAndDelete(configDir, moccaDir.toURI(), zipOS);
          zipOS.close();
          initConfig(configDir);
        }
      }
    } else {
      initConfig(configDir);
    }
  }

  /**
   * To be replaced by TSLs in IAIK-PKI
   * @throws IOException
   */
  public void ensureCertificates() throws IOException {
    File certsDir = new File(System.getProperty("user.home") + '/' + CERTS_DIR);
    if (certsDir.exists()) {
      if (certsDir.isFile()) {
        log.error("invalid certificate store directory: " + certsDir);
        throw new IOException("invalid config directory: " + certsDir);
      } else {
        certsVersion = readVersion(new File(certsDir, VERSION_FILE));
        if (log.isDebugEnabled()) {
          log.debug("certificate-store directory " + certsDir + ", version " + certsVersion);
        }
        String newCertsVersion = getCertificatesVersion();
        if (updateRequiredStrict(certsVersion, newCertsVersion)) {
          File moccaDir = certsDir.getParentFile();
          File zipFile = new File(moccaDir, "certs-" + certsVersion + ".zip");
          ZipOutputStream zipOS = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
          log.info("backup certificates to " + zipFile);
          backupAndDelete(certsDir, moccaDir.toURI(), zipOS);
          zipOS.close();

          createCerts(certsDir, newCertsVersion);
          certsVersion = newCertsVersion;
        }
      }
    } else {
      String newCertsVersion = getCertificatesVersion();
      createCerts(certsDir, newCertsVersion);
      certsVersion = newCertsVersion;
    }
  }

  /**
   * 
   * @return whether a new MOCCA TLS certificate has been created during initialization
   */
  public boolean isCertRenewed() {
    return certRenewed;
  }

  /**
   * @return The first valid (not empty, no comment) line of the version file or
   * "unknown" if version file cannot be read or does not contain such a line.
   */
  protected static String readVersion(File versionFile) {
    if (versionFile.exists() && versionFile.canRead()) {
      BufferedReader versionReader = null;
      try {
        versionReader = new BufferedReader(new FileReader(versionFile));
        String version;
        while ((version = versionReader.readLine().trim()) != null) {
          if (version.length() > 0 && !version.startsWith("#")) {
            log.debug("configuration version from " + versionFile + ": " + version);
            return version;
          }
        }
      } catch (IOException ex) {
        log.error("failed to read configuration version from " + versionFile, ex);
      } finally {
        try {
          versionReader.close();
        } catch (IOException ex) {
        }
      }
    }
    log.debug("unknown configuration version");
    return UNKOWN_VERSION;
  }

  /**
   * Temporary workaround, replace with TSLs in IAIK-PKI.
   * Retrieves version from BKUCertificates.jar Manifest file. 
   * The (remote) resource URL will be handled by the JNLP loader, 
   * and the resource retrieved from the cache.
   *
   * @return
   * @throws IOException
   */
  private static String getCertificatesVersion() throws IOException {
    String certsResourceVersion = null;
    URL certsURL = Configurator.class.getClassLoader().getResource(CERTIFICATES_PKG);
    if (certsURL != null) {
      StringBuilder url = new StringBuilder(certsURL.toExternalForm());
      url = url.replace(url.length() - CERTIFICATES_PKG.length(), url.length(), "META-INF/MANIFEST.MF");
      log.trace("retrieve certificates resource version from " + url);
      certsURL = new URL(url.toString());
      Manifest certsManifest = new Manifest(certsURL.openStream());
      Attributes atts = certsManifest.getMainAttributes();
      if (atts != null) {
        certsResourceVersion = atts.getValue("Implementation-Version");
        log.debug("certs resource version: " + certsResourceVersion);
      }
    } else {
      log.error("Failed to retrieve certificates resource " + CERTIFICATES_PKG);
      throw new IOException("Failed to retrieve certificates resource " + CERTIFICATES_PKG);
    }
    return certsResourceVersion;
  }

  protected static boolean updateRequired(String oldVersion) {
     log.debug("comparing " + oldVersion + " to " + MIN_CONFIG_VERSION);
     if (oldVersion != null && !UNKOWN_VERSION.equals(oldVersion)) {
     
      int majorEnd = oldVersion.indexOf('-');
      String oldMajor = (majorEnd < 0) ? oldVersion : oldVersion.substring(0, majorEnd);

      String minMajor = MIN_CONFIG_VERSION;
      boolean releaseRequired = true;
      if (MIN_CONFIG_VERSION.endsWith("-SNAPSHOT")) {
        releaseRequired = false;
        minMajor = minMajor.substring(0, minMajor.length() - 9);
      }

      int compare = oldMajor.compareTo(minMajor);
      if (compare < 0 ||
              // SNAPSHOT versions are pre-releases (update if release required)
              (compare == 0 && releaseRequired && oldVersion.startsWith("-SNAPSHOT", majorEnd))) {
        log.debug("configuration update required");
        return true;
      } else {
        log.debug("configuration up to date");
        return false;
      }
    }
    log.debug("no old version, configuration update required");
    return true;
  }

  /**
   * if unknown old, update in any case
   * if known old and unknown new, don't update
   * @param oldVersion
   * @param newVersion
   * @return
   */
  private boolean updateRequiredStrict(String oldVersion, String newVersion) {
    log.debug("comparing " + oldVersion + " to " + newVersion);
    if (oldVersion != null && !UNKOWN_VERSION.equals(oldVersion)) {
      if (newVersion != null && !UNKOWN_VERSION.equals(newVersion)) {
        String[] oldV = oldVersion.split("-");
        String[] newV = newVersion.split("-");
        log.trace("comparing " + oldV[0] + " to " + newV[0]);
        if (oldV[0].compareTo(newV[0]) < 0) {
          log.debug("update required");
          return true;
        } else {
          log.trace("comparing " + oldV[oldV.length - 1] + " to " + newV[newV.length - 1]);
          if (oldV[oldV.length - 1].compareTo(newV[newV.length - 1]) < 0) {
            log.debug("update required");
            return true;
          } else {
            log.debug("no update required");
            return false;
          }
        }
      }
      log.debug("unknown new version, do not update");
      return true;
    }
    log.debug("unknown old version, update required");
    return true;
  }
  
  protected static void backupAndDelete(File dir, URI relativeTo, ZipOutputStream zip) throws IOException {
    if (dir.isDirectory()) {
      File[] subDirs = dir.listFiles();
      for (File subDir : subDirs) {
        backupAndDelete(subDir, relativeTo, zip);
        subDir.delete();
      }
    } else {
      URI relativePath = relativeTo.relativize(dir.toURI());
      ZipEntry entry = new ZipEntry(relativePath.toString());
      zip.putNextEntry(entry);
      BufferedInputStream entryIS = new BufferedInputStream(new FileInputStream(dir));
      StreamUtil.copyStream(entryIS, zip);
      entryIS.close();
      zip.closeEntry();
      dir.delete();
    }
  }

  /**
   * set up a new MOCCA local configuration
   * (not to be called directly, call ensureConfiguration())
   * @throws IOException config/certificate creation failed
   * @throws GeneralSecurityException if MOCCA TLS certificate could not be created
   * @throws CodingException if MOCCA TLS certificate could not be created
   */
  protected void initConfig(File configDir) throws IOException, GeneralSecurityException, CodingException {
    createConfig(configDir, Launcher.version);
    version = Launcher.version;
    createKeyStore(configDir);
    certRenewed = true;
  }

  private static void createConfig(File configDir, String version) throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("creating configuration version " + Launcher.version + " in " + configDir );
    }
    configDir.mkdirs();
    File confTemplateFile = new File(configDir, CONF_TEMPLATE_FILE);
    InputStream is = Configurator.class.getClassLoader().getResourceAsStream(CONF_TEMPLATE_RESOURCE);
    OutputStream os = new BufferedOutputStream(new FileOutputStream(confTemplateFile));
    StreamUtil.copyStream(is, os);
    os.close();
    unzip(confTemplateFile, configDir);
    confTemplateFile.delete();
    writeVersionFile(new File(configDir, VERSION_FILE), version);
  }

  /**
   * set up a new MOCCA local certStore
   * @throws IOException config/certificate creation failed
   * @throws GeneralSecurityException if MOCCA TLS certificate could not be created
   * @throws CodingException if MOCCA TLS certificate could not be created
   */
  private static void createCerts(File certsDir, String certsVersion) throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("creating certificate-store " + certsDir + ", version " + certsVersion);
    }
    URL certsURL = Configurator.class.getClassLoader().getResource(CERTIFICATES_PKG);
    if (certsURL != null) {
      StringBuilder url = new StringBuilder(certsURL.toExternalForm());
      url = url.replace(url.length() - CERTIFICATES_PKG.length(), url.length(), "META-INF/MANIFEST.MF");
      log.debug("retrieve certificate resource names from " + url);
      certsURL = new URL(url.toString());
      Manifest certsManifest = new Manifest(certsURL.openStream());
      certsDir.mkdirs();
      Iterator<String> entries = certsManifest.getEntries().keySet().iterator();
      while (entries.hasNext()) {
        String entry = entries.next();
        if (entry.startsWith(CERTIFICATES_PKG)) {
          String f = entry.substring(CERTIFICATES_PKG.length()); // "/trustStore/..."
          new File(certsDir, f.substring(0, f.lastIndexOf('/'))).mkdirs();
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(certsDir, f)));
          log.debug(f);
          StreamUtil.copyStream(Configurator.class.getClassLoader().getResourceAsStream(entry), bos);
          bos.close();
        } else {
          log.trace("ignore " + entry);
        }
      }
      writeVersionFile(new File(certsDir, VERSION_FILE), certsVersion);
    } else {
      log.error("Failed to retrieve certificates resource " + CERTIFICATES_PKG);
      throw new IOException("Failed to retrieve certificates resource " + CERTIFICATES_PKG);
    }
  }

  private static void unzip(File zipfile, File toDir) throws IOException {
    ZipFile zipFile = new ZipFile(zipfile);
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      File eF = new File(toDir, entry.getName());
      if (entry.isDirectory()) {
        eF.mkdirs();
        continue;
      }
      File f = new File(eF.getParent());
      f.mkdirs();
      StreamUtil.copyStream(zipFile.getInputStream(entry),
              new FileOutputStream(eF));
    }
    zipFile.close();
  }

  private static void writeVersionFile(File versionFile, String version) throws IOException {
    BufferedWriter versionWriter = new BufferedWriter(new FileWriter(versionFile));
    versionWriter.write("# MOCCA Web Start configuration version\n");
    versionWriter.write("# DO NOT MODIFY THIS FILE\n\n");
    versionWriter.write(version);
    versionWriter.close();
  }

  private static void createKeyStore(File configDir) throws IOException, GeneralSecurityException, CodingException {
    char[] password = UUID.randomUUID().toString().toCharArray();
    File passwdFile = new File(configDir, PASSWD_FILE);
    FileWriter passwdWriter = new FileWriter(passwdFile);
    passwdWriter.write(password);
    passwdWriter.close();
    if (!passwdFile.setReadable(false, false) || !passwdFile.setReadable(true, true)) {
      passwdFile.delete();
      throw new IOException("failed to make " + passwdFile + " owner readable only, deleting file");
    }
    TLSServerCA ca = new TLSServerCA();
    KeyStore ks = ca.generateKeyStore(password);
    File ksFile = new File(configDir, KEYSTORE_FILE);
    FileOutputStream fos = new FileOutputStream(ksFile);
    ks.store(fos, password);
    fos.close();
  }
}