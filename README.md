Sandbox
=======

Mainly to store testing code.

To setup Pentaho Kettle at Eclipse.

A. Eclipse + IvyDE

1. Install IvyDE
2. At Window -> Preference -> Ivy -> Settings, check reload the setting on demand, add Ivy setting path `ivysettings.xml`, Property files `build.properties`.
3. At Window -> Preference -> Ivy -> Classpath container, check Resolve dependencies in workspace.
6. Replace jar `%UserProfile%/.ivy2/cache/org.eclipse.swt/swt-linux-x86_64/jars` with OS swt.jar (rename)
7. Change `<dependency org="org.eclipse.swt" name="swt-linux-x86_64" rev="3.7" transitive="false" />` in UI project to `<dependency org="org.eclipse.swt" name="swt-win32" rev="3.3.0.3346" transitive="false" />`.
8. Add new Java project, point the path to pentaho project.
9. Add Ivy managed dependencies library in new project wizard.

B. Pure Eclipse setting

1. Add `<dependency org="mysql"	name="mysql-connector-java"	rev="5.1.30"/>` at root ivy.xml
2. Add assembly/package-res/ui in ui/src folder. (prefer softlink)
3. Everything when a new dependency is added, run `ant resolve create-dot-classpath` and refresth Eclipse project
