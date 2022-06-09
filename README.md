# jfxoo

![Stability-wip](https://img.shields.io/badge/Stability-WIP-yellow.svg?style=flat-square)

[Annotation processor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html) library
to generate [JavaFX](https://openjfx.io/) Form (GridPane) during compile time.

### Usage

This library is not yet stable so it's not yet published to maven central, but you can find it in snapshot repository

#### In build.gradle

```gradle
repositories {
    //...
    maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots' }
}
dependencies {
    implementation 'io.github.tanialx:jfxoo:1.0.0-SNAPSHOT'
    annotationProcessor 'io.github.tanialx:jfxoo:1.0.0-SNAPSHOT'
}
```

#### In .java source code

##### Form

Annotate object with `@JFXooForm`

```java
@JFXooForm
public class Contact {
  private String name;
  private String phone;
  private String email;
}
```

Init JFXoo factory class

```java
JFXoo jfxoo = JFXoo.init();
```

Retrieve the generate form to add to parent view

```java
JFXooForm<Contact> contactForm = jfxoo.get("Contact", Contact.class);
Node form = contactForm.node(); // retrieve inner node property within JFXooForm

// registered buttons to be displayed at the bottom of the form
//
loginForm.button("Log in", logins -> {
    // do something on button clicked
}
```

##### Table

Annotate object with `@JFXooTable`

```java
JFXooTable<Contact> contactTable = jfxoo.table("Contact", Contact.class);

// javafx node to added to scene
//
Node node = contactTable.node(); // table and basic control buttons (as VBox)
Node table = contactTable.table(); // only table
```
