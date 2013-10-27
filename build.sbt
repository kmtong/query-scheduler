name := "query-scheduler"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "commons-io" % "commons-io" % "2.2",
  "com.google.inject" % "guice" % "3.0",
  "org.apache.camel" % "camel-core" % "2.12.1",
  "org.apache.camel" % "camel-quartz" % "2.12.1"
)     

play.Project.playJavaSettings
