Sandbox
=======

Mainly to store testing code.

To setup Pentaho Kettle at Eclipse. The ivy files provided by Pentaho is not compatible with IvyDE, where IvyDE does not read build.properties automatically.

1. Add \<dependency org="mysql"	name="mysql-connector-java"	rev="5.1.30"/> at root ivy.xml
2. Add assembly/package-res/ui in ui/src folder. (prefer softlink)
3. Everything when a new dependency is added, run ant resolve create-dot-classpath and refresth Eclipse project
