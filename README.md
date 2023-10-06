<a name="readme-top"></a>

<br />
<div align="center">
    <a href="https://github.com/IvanEOD/scriptable/blob/master/README.md"></a>
    <h1 align="center">Scriptable (Kotlin)</h1>

---
<p align="center">
    Scriptable implementation in Kotlin
    </p>
</div>

---

<details>
    <summary>Table of Contents</summary>
    <ul>
        <li><a href="#about-the-project">About the Project</a>
            <ul>
                <li><a href="#built-with">Built With</a></li>
            </ul>
        </li>
        <li><a href="#🛠-project-configuration-🛠">Project Configuration</a>
        <ul>
            <li><a href="#environment">environment</a></li>
            <li><a href="#properties">properties</a></li>
            <li><a href="#icloudscriptabledirectory">iCloudScriptableDirectory</a></li>
            <li><a href="#defaulticon">defaultIcon</a></li>
            <li><a href="#defaultcolor">defaultColor</a></li>
            <li><a href="#include">include</a></li>
        </ul>
    </ul>

</details>

## About the Project

- [Kotlin][Kotlin Link] implementation of [Scriptable][Scriptable Link] for iOS, utilizing [Dukat][dukat Link] to generate [external declarations][External Declarations Link] which provide type information from the [iOS Scriptable Types][ios-scriptable-types Link] project. 
- Once you have written your own scripts you can use the [Scriptable][Scriptable Link] app to run them on your iOS device.
- Uses [esbuild][esbuild] to bundle each script into its own file, only including dependencies that are actually used by the script.
- To be able to efficiently create your own scripts you will need a basic understanding of [Kotlin][Kotlin Link].

### Built With


* [![Kotlin/JS][Kotlin Image]][Kotlin Link]
* [![Gradle][Gradle Image]][Gradle Link]
* [![Intellij Idea][Intellij Idea Image]][Intellij Idea Link]
* [![Scriptable][Scriptable Image]][Scriptable Link]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## 🛠 Project Configuration 🛠

<details><summary>

##### The Scriptable Extension

</summary>


---

<a name="build-gradle-kts"></a>



###### The root [build.gradle.kts](build.gradle.kts) file implements the [main plugin](gradle/build-logic/src/main/kotlin/scriptable/main/ScriptableMain.kt), which is responsible for generating the projects used for each Scriptable script.


- The project is configured using the [scriptable extension](gradle/build-logic/src/main/kotlin/scriptable/main/ScriptableExtension.kt):
    - ```kotlin
        plugins {
            id("scriptable-main")
        }
      
        scriptable {
            // ...
        }
      ```

      - ### environment (name) <a name="environment"></a>
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
      - ### defaultIcon [[ScriptIcon](gradle/build-logic/src/main/kotlin/ScriptIcon.kt)]
        - Not required.
        - Defaults to `ScriptIcon.Desktop`
        - This is the value that will be implemented with `ScriptIcon.Default`
        - ```kotlin
          defaultIcon.set(ScriptIcon.Desktop)
          ```
      - ### defaultColor [[ScriptColor](gradle/build-logic/src/main/kotlin/ScriptColor.kt)]
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
              - [ScriptIcon](gradle/build-logic/src/main/kotlin/ScriptIcon.kt) enum. (e.g. `ScriptIcon.Desktop`, `ScriptIcon.UserShield`)
              - ScriptIcon enum name. (e.g. `"Desktop"`, `"UserShield"`)
              - String value shown in the Scriptable app. (e.g. `"desktop"`, `"user-shield"`)
          - **color**:
            - Not required.
            - Defaults to `ScriptColor.Default`, which uses the project default defined above.
            - Acceptable values are:
              - [ScriptColor](gradle/build-logic/src/main/kotlin/ScriptColor.kt) enum. (e.g. `ScriptColor.DeepGray`, `ScriptColor.DeepBlue`)
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
</details>



[Scriptable Link]: https://scriptable.app/
[Scriptable Docs Link]: https://docs.scriptable.app/
[Scriptable Image]: https://img.shields.io/badge/Scriptable-1.7.10_(2)-yellowgreen.svg?logo=data:image/svg%2bxml;base64,PGltZyBzcmM9Imh0dHBzOi8vZG9jcy5zY3JpcHRhYmxlLmFwcC9pbWcvZ2x5cGgucG5nIj48L2ltZz4

[ios-scriptable-types Link]: https://github.com/schl3ck/ios-scriptable-types
[esbuild]: https://github.com/evanw/esbuild
[build-gradle-kts]: https://TODO()
[main-plugin]: https://TODO()
[scriptable-extension]: https://TODO()
[dukat Link]: https://github.com/Kotlin/dukat
[External Declarations Link]: https://kotlinlang.org/docs/js-interop.html#external-modifier

[Kotlin DSL Link]: https://docs.gradle.org/current/userguide/kotlin_dsl.html

[Kotlin Link]: https://kotlinlang.org/
[Kotlin Image]: https://img.shields.io/badge/Kotlin/JS-1.9.20Beta--2-yellowgreen.svg?logo=kotlin&style=flat
[Gradle Link]: https://gradle.org/
[Gradle Image]: https://img.shields.io/badge/Gradle-8.3-yellowgreen.svg?logo=gradle&style=flat
[Intellij Idea Link]: https://www.jetbrains.com/idea/
[Intellij Idea Image]: https://img.shields.io/badge/Intellij-2023.2.2-yellowgreen.svg?logo=intellij-idea&style=flat