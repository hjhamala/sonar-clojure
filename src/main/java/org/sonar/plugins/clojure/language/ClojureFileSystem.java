/*
 * Sonar Groovy Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.clojure.language;

import org.sonar.api.batch.BatchSide;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;

import javax.annotation.CheckForNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@BatchSide
public class ClojureFileSystem {

  private final FileSystem fileSystem;
  private final FilePredicates predicates;
  private final FilePredicate isClojureLanguage;
  private final FilePredicate isMainTypeFile;

  public ClojureFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.predicates = fileSystem.predicates();
    this.isClojureLanguage = predicates.hasLanguage(ClojureLanguage.KEY);
    this.isMainTypeFile = predicates.hasType(Type.MAIN);
  }

  public boolean hasGroovyFiles() {
    return fileSystem.hasFiles(isClojureLanguage);
  }

  public List<File> sourceFiles() {
    Iterable<File> files = fileSystem.files(predicates.and(isClojureLanguage, isMainTypeFile));
    List<File> list = new ArrayList<>();
    files.iterator().forEachRemaining(list::add);
    return list;
  }

  public List<InputFile> groovyInputFiles() {
    Iterable<InputFile> inputFiles = fileSystem.inputFiles(isClojureLanguage);
    List<InputFile> list = new ArrayList<>();
    inputFiles.iterator().forEachRemaining(list::add);
    return list;
  }

  public List<InputFile> sourceInputFiles() {
    Iterable<InputFile> inputFiles = fileSystem.inputFiles(predicates.and(isClojureLanguage, isMainTypeFile));
    List<InputFile> list = new ArrayList<>();
    inputFiles.iterator().forEachRemaining(list::add);
    return list;
  }

  @CheckForNull
  public InputFile sourceInputFileFromRelativePath(String relativePath) {
    return fileSystem.inputFile(predicates.and(predicates.matchesPathPattern("**/" + relativePath), isClojureLanguage, isMainTypeFile));
  }

  public File baseDir() {
    return fileSystem.baseDir();
  }

}
