Sandbox
=======

Mainly to store testing code.

To setup Pentaho Kettle at Eclipse.

Dependency setup/Code Change

1. Add `<dependency org="mysql"	name="mysql-connector-java"	rev="5.1.30"/>` at UI ivy.xml.
2. Add assembly/package-res/ui in ui/src folder. (prefer softlink)
3. Replace jar `%UserProfile%/.ivy2/cache/org.eclipse.swt/swt-linux-x86_64/jars` with OS swt.jar (rename)
4. Change `org.pentaho.di.ui.spoon.MainSpoonPerspective.getPerspectiveIcon()` to `return Thread.currentThread().getContextClassLoader().getResourceAsStream("ui/images/transformation.png");`
7. Change `<dependency org="org.eclipse.swt" name="swt-linux-x86_64" rev="3.7" transitive="false" />` in UI project to `<dependency org="org.eclipse.swt" name="swt-win32" rev="3.3.0.3346" transitive="false" />`.

A. Eclipse + IvyDE (at individual Pentaho project)

1. Install IvyDE from http://www.apache.org/dist/ant/ivyde/updatesite (ref: http://ant.apache.org/ivy/ivyde/download.cgi)
2. At Window -> Preference -> Ivy -> Settings, check reload the setting on demand, add Ivy setting path `ivysettings.xml`, Property files `build.properties`.
3. At Window -> Preference -> Ivy -> Classpath container, check Resolve dependencies in workspace.
8. Add new Java project at project you want to work on, point the path to pentaho project.
9. Add Ivy managed dependencies library in new project wizard.

B. Pure Eclipse setting (at root level for all Pentaho projects)

3. run `ant resolve create-dot-classpath`
4. Import Existing Eclipse project at root level.
4. Everything when a new dependency is added, run `ant resolve create-dot-classpath` and refresth Eclipse project.
