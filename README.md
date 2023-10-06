<div align="center">
    <h1 align="center">Scriptable (Kotlin)</h1>

---

<p align="center">
    Scriptable implementation in Kotlin
    </p>
</div>

---

<details>
    <summary><a href="#table-of-contents">Table of Contents</a></summary>
    <ul>
        <li><a href="#about-the-project">About the Project</a>
            <ul>
                <li><a href="#built-with">Built With</a></li>
            </ul>
        </li>
        <li><a href="#-project-configuration-">Project Configuration</a>
        <ul>
            <li><a href="#environment-name">Environment</a></li>
            <li><a href="#properties-name">Properties</a></li>
            <li><a href="#icloudscriptabledirectory">iCloud Scriptable Directory</a></li>
            <li><a href="#defaulticon-scripticon-">Default Icon</a></li>
            <li><a href="#defaultcolor-scriptcolor">Default Color</a></li>
            <li><a href="#includename-modulename-icon-color">Include script</a></li>
            <li><a href="#includename-icon-color">Include script (alternate)</a></li>
            <li><a href="#a-full-example-might-look-like">Example configuration</a></li>
        </ul>
    </ul>

</details>

## About the Project

- [Kotlin][Kotlin Link] implementation of [Scriptable][Scriptable Link] for iOS, utilizing [Dukat][dukat Link] to generate [external declarations][External Declarations Link] which provide type information from the [iOS Scriptable Types][ios-scriptable-types Link] project. 
- Once you have written your own scripts you can use the [Scriptable][Scriptable Link] app to run them on your iOS device.
- Uses [esbuild][esbuild] to bundle each script into its own file, only including dependencies that are actually used by the script.
- To be able to efficiently create your own scripts you will need a basic understanding of [Kotlin][Kotlin Link].

> [!WARNING]
> This is a new project and is still in development.
> Let me know if you run into any issues or have any questions.

### Built With


* [![Kotlin/JS][Kotlin Image]][Kotlin Link]
* [![Gradle][Gradle Image]][Gradle Link]
* [![Intellij Idea][Intellij Idea Image]][Intellij Idea Link]
* [![Scriptable][Scriptable Image]][Scriptable Link]

### Special thanks to the creators of all projects utilized by this project!

 ###### Including but not limited to
- [@simonbs](https://github.com/simonbs)
- [@schl3ck](https://github.com/schl3ck)
- [@evanw](https://github.com/evanw)

<p align="right">(<a href="#scriptable-kotlin">back to top</a>)</p>

<details><summary>

## 🛠 Project Configuration 🛠

</summary>


---

###### The root [build.gradle.kts](build.gradle.kts) file implements the [main plugin][Main Plugin Link], which is responsible for generating the projects used for each Scriptable script.


- The project is configured using the [scriptable extension][Extension Link]:

    - ```kotlin
        plugins {
            id("scriptable-main")
        }
      
        scriptable {
            // ...
        }
      ```

      - ### environment (name)
        - Returns the value of the specified environment variable, or ***throws an error if the variable is not defined***.
        - This is a shortcut for `providers.environmentVariable(name).get()`.
        - If the environment variable has only been set since your IDE has been open, you may need to restart your IDE for the change to take effect.
        - ```kotlin
          environment("ICLOUD_SCRIPTABLE_DIRECTORY")
          ```
      - ### properties (name)

          - Returns the value of the specified Gradle property, or ***throws an error if the property is not defined***.
          - This is a shortcut for `providers.gradleProperty(name).get()`.
          - ```kotlin
            properties("icloud.drive.path")
            ```

      -  ### iCloudScriptableDirectory
          - **Required**.
          - You should have iCloud Drive setup on your PC, and this should be the path to the Scriptable folder in your iCloud Drive.
          - This is where the script files will be copied to when you run the "sync" task.
          - ```kotlin
            iCloudScriptableDirectory.set(file(environment("ICLOUD_SCRIPTABLE_DIRECTORY")))
            // or
            iCloudScriptableDirectory.set(file(properties("icloud.drive.path")))
            // or
            iCloudScriptableDirectory.set(file("path/to/icloud/drive/Scriptable"))
            ```
      - ### defaultMinifyScripts
          - Not required.
          - Defaults to `true`
          - If set to `true`, the scripts will be minified when they are bundled, helping keep the file size to a minimum.
          - ```kotlin
            defaultMinifyScripts.set(false)
            ```
      - ### defaultIcon [[ScriptIcon][Script Icon Link]] <a name="defaulticon"></a>
        - Not required.
        - Defaults to `ScriptIcon.Desktop`
        - This is the value that will be implemented with `ScriptIcon.Default`
        - ```kotlin
          defaultIcon.set(ScriptIcon.Desktop)
          ```
      - ### defaultColor [[ScriptColor][Script Color Link]]
        - Not required.
        - Defaults to `ScriptColor.DeepGray`
        - This is the value that will be implemented with `ScriptColor.Default`
        - ```kotlin
          defaultColor.set(ScriptColor.DeepGray)
          ```

      - ### include(name, moduleName, icon, color)
        - Adds a scriptable to the project, automatically creating non-existent projects within the [scripts](scripts) project.
        - Scripts removed from this will not be deleted automatically, but will not be included in the project build any longer.
          - **name**:
            - **Required**.
            - The name of the scriptable. This is the name for the script as shown in the Scriptable app.
          - **module**:
            - Not required.
            - Defaults to name.toKebabCase() (e.g. `"My Script"` becomes `"my-script"`).
          - **icon**:
            - Not required.
            - Defaults to `ScriptIcon.Default`, which uses the project default defined above.
            - Acceptable values are:
              - [ScriptIcon][Script Icon Link] enum. (e.g. `ScriptIcon.Desktop`, `ScriptIcon.UserShield`)
              - ScriptIcon enum name. (e.g. `"Desktop"`, `"UserShield"`)
              - String value shown in the Scriptable app. (e.g. `"desktop"`, `"user-shield"`)
          - **color**:
            - Not required.
            - Defaults to `ScriptColor.Default`, which uses the project default defined above.
            - Acceptable values are:
              - [ScriptColor][Script Color Link] enum. (e.g. `ScriptColor.DeepGray`, `ScriptColor.DeepBlue`)
              - ScriptColor enum name. (e.g. `"DeepGray"`, `"DeepBlue"`)
              - String value shown in the Scriptable app. (e.g. `"deep-gray"`, `"deep-blue"`)
        - ```kotlin
          // You can use any combination of the accepted icon / color formats.
          include("My Script", "my-script", ScriptIcon.Desktop, ScriptColor.DeepGray)
          // or
          include("My Script", "my-script", "Desktop", "DeepGray")
          // or 
          include("My Script", "my-script", "desktop", "deep-gray")
          // or
          include("My Script", "my-script", "Desktop", ScriptColor.DeepGray)
          // or 
          include("My Script", "my-script", "desktop", ScriptColor.DeepGray)
          // or
          include("My Script", "my-script", ScriptIcon.Desktop, "DeepGray")
          
          // Or leave them off to use your project defaults
          include("My Script", "my-script")
          ``` 
          
      - ### include(name, icon, color)
        - Shortcut for include described above, automatically using `name.toKebabCase()` for the module name.
  
      - ### A full example might look like:
        - ```kotlin
          scriptable {

            iCloudScriptableDirectory.set(file(environment("SCRIPTABLE_ICLOUD_PATH")))
            iCloudScriptableCacheDirectory.set(file(environment("SCRIPTABLE_DATA_PATH")))
        
            defaultMinifyScripts.set(false)
            defaultIcon.set(ScriptIcon.AddressCard)
            defaultColor.set(ScriptColor.DeepPurple)
        
            include("ShowTableExample", "show-table-example", ScriptIcon.Table, ScriptColor.DeepGreen)
            include("show-alert-example", color = ScriptColor.DeepOrange)
        
          }
          ``` 

<p align="right">(<a href="#scriptable-kotlin">back to top</a>)</p>

</details>

<details><summary>

## 🔍 How it works 🔍

</summary>


---

> [!IMPORTANT]
>  - You need to have iCloud Drive setup on your PC, and you need to have the Scriptable app installed on your iOS device.
>  - Once you've done that, you need to set up the [project configuration](#-project-configuration-).
>  - If you want to use your system environment for the iCloud Path, you will need to restart your IDE for the change to take effect, unless you happened to have it saved already.

- ### The [main plugin][Main Plugin Link]
    - Responsible for generating the projects used for each Scriptable script.
    - To add a script, you need to add it to the [project configuration](#-project-configuration-).
    - To remove a script, you need to first remove it from the configuration, and then manually delete the files. If you don't remove it from the configuration, it will repopulate to the default new script setup.
    
- ### The [initialize task][Initialize Task Link] 
  - Triggers evaluation of your configuration and applies any changes you have made.
  - This should be triggered automatically, but if you need to manually trigger it you can run the `initialize` task.
  - This task will automatically run before the `sync` task.
  - Only scripts that have been added, changed, or removed will be effected by this task.
  - This task will not delete any files, only create or update them.

- ### The [sync task][Sync Task Link] 
  - Builds the project, processing it into javascript code that can be run by the Scriptable app.
  - Packages the scripts into their own files, only including dependencies that are actually used by the script.
  - Copies the scripts to the iCloud Scriptable directory, so you can run/test them directly on you iOS device.
  - Only processes scripts that have been effected by changes you have made since the last sync. 
  - This can be applied per script in the gradle menu, or in the root project to sync all scripts.

- ### The [scriptable project][Scriptable Project Link]
  - Is where the declarations for the Scriptable API are stored. The [ios-scriptable-types][ios-scriptable-types Link] project is used to generate these declarations.
  - This project is automatically included in the [scripts project][Scripts Project Link] and the [library project][Library Project Link].
  - None of this will be compiled into your script, it will use the declarations to provide type information for the Scriptable API.

- ### The [library project][Library Project Link]
  - Is where you can create your own libraries to be used by your scripts.
  - The scripts will automatically have access to this library, and will only include what they need from it when they are bundled.
  
- ### The [scripts project][Scripts Project Link]
  - Is where you can create your own scripts.
  - A project will be created in this directory for each script you add to the [project configuration](#-project-configuration-).
  - The scripts will automatically have access to the [scriptable project][Scriptable Project Link] and the [library project][Library Project Link], and will only include what they need from them when they are bundled.

</details>

<details><summary>

## 📚 Useful Information 📚

</summary>


---

- ### Miscellaneous
  -  The `gradle.properties` file in each script project is updated automatically, changes made to it will be overwritten automatically. Make changes in the root `build.gradle.kts` file.

- ### Links
  - [Scriptable App][Scriptable Link]
  - [Scriptable Docs][Scriptable Docs Link]
  - [iOS Scriptable Types][ios-scriptable-types Link]
  - [Kotlin][Kotlin Link]
  - [Kotlin DSL][Kotlin DSL Link]
  - [Gradle][Gradle Link]
  - [Intellij Idea][Intellij Idea Link]
  - [esbuild][esbuild]
  - [dukat][dukat Link]
  - [External Declarations][External Declarations Link]



</details>


[Scriptable Link]: https://scriptable.app/
[Scriptable Docs Link]: https://docs.scriptable.app/
[Scriptable Image]: https://img.shields.io/badge/Scriptable-1.7.10_(2)-yellowgreen.svg?logo=data:image/svg%2bxml;base64,PGltZyBzcmM9Imh0dHBzOi8vZG9jcy5zY3JpcHRhYmxlLmFwcC9pbWcvZ2x5cGgucG5nIj48L2ltZz4

[ios-scriptable-types Link]: https://github.com/schl3ck/ios-scriptable-types
[esbuild]: https://github.com/evanw/esbuild
[dukat Link]: https://github.com/Kotlin/dukat
[External Declarations Link]: https://kotlinlang.org/docs/js-interop.html#external-modifier

[Kotlin DSL Link]: https://docs.gradle.org/current/userguide/kotlin_dsl.html

[Kotlin Link]: https://kotlinlang.org/
[Kotlin Image]: https://img.shields.io/badge/Kotlin/JS-1.9.20Beta--2-yellowgreen.svg?logo=kotlin&style=flat
[Gradle Link]: https://gradle.org/
[Gradle Image]: https://img.shields.io/badge/Gradle-8.3-yellowgreen.svg?logo=gradle&style=flat
[Intellij Idea Link]: https://www.jetbrains.com/idea/
[Intellij Idea Image]: https://img.shields.io/badge/Intellij-2023.2.2-yellowgreen.svg?logo=intellij-idea&style=flat


[Script Icon Link]: gradle/build-logic/src/main/kotlin/ScriptIcon.kt
[Script Color Link]: gradle/build-logic/src/main/kotlin/ScriptColor.kt

[Main Plugin Link]: gradle/build-logic/src/main/kotlin/scriptable/main/ScriptableMain.kt
[Extension Link]: gradle/build-logic/src/main/kotlin/scriptable/main/ScriptableExtension.kt
[Initialize Task Link]: https://github.com/IvanEOD/scriptable-kotlin/blob/8bcdec20f517bf954db3be8ed03c64778c0a2ffa/gradle/build-logic/src/main/kotlin/scriptable/main/ScriptableMain.kt#L68C61-L68C61
[Sync Task Link]: https://github.com/IvanEOD/scriptable-kotlin/blob/8bcdec20f517bf954db3be8ed03c64778c0a2ffa/gradle/build-logic/src/main/kotlin/scriptable/script/ScriptableScript.kt#L124

[Scriptable Project Link]: scriptable
[Library Project Link]: library
[Scripts Project Link]: scripts
