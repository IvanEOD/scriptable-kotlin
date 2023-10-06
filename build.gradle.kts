plugins {
    kotlin("multiplatform") apply false
    id("scriptable-main")
}

/*

    environment(String):
        - Returns the value of the specified environment variable, or throws an error if the variable is not defined.
        - This is a shortcut for providers.environmentVariable(name).get().
        - If the environment variable has only been set since your IDE has been open, you may need to restart your IDE for the change to take effect.

    properties(String):
        - Returns the value of the specified Gradle property, or throws an error if the property is not defined.
        - This is a shortcut for providers.gradleProperty(name).get().

    iCloudScriptableDirectory:
        - Required.
        - You should have iCloud Drive setup on your PC, and this should be the path to the Scriptable folder in your iCloud Drive.
        - This is where the scriptable files will be copied to when you run the "sync" task.

    iCloudScriptableCacheDirectory:
        - Not required.
        - Defaults to iCloudScriptableDirectory.parentFile.resolve("ScriptableData"), and creates the directory if it does not exist.
        - This is where any files Scriptable may need will be cached and accessible to your scripts, to avoid cluttering the Scriptable folder.

    defaultIcon:
        - Not required.
        - Defaults to ScriptIcon.Desktop.
        - This is the default icon that will be used for any scriptable that does not specify an icon, or uses ScriptIcon.Default.

    defaultColor:
        - Not required.
        - Defaults to ScriptColor.DeepGray.
        - This is the default color that will be used for any scriptable that does not specify a color, or uses ScriptColor.Default.

    include(name: String, moduleName: String, icon: Any, color: Any):
        - Adds a scriptable to the project, automatically creating non-existent projects within the `scripts` project.
        - Scripts removed from this will not be deleted automatically, but will not be included in the project build any longer.
        - Usage:
            - name:
                - Required.
                - The name of the scriptable. This is the name for the script as shown in the Scriptable app.
            - module:
                - Not required.
                - Defaults to name.toKebabCase() (e.g. "My Script" becomes "my-script").
            - icon:
                - Not required.
                - Defaults to ScriptIcon.Default, which uses the project default defined above.
                - Acceptable values are:
                    - ScriptIcon enum. (e.g. ScriptIcon.Desktop, ScriptIcon.UserShield)
                    - ScriptIcon enum name. (e.g. "Desktop", "UserShield")
                    - String value shown in the Scriptable app. (e.g. "desktop", "user-shield")
            - color:
                - Not required.
                - Defaults to ScriptColor.Default, which uses the project default defined above.
                - Acceptable values are:
                    - ScriptColor enum. (e.g. ScriptColor.DeepGray, ScriptColor.DeepBlue)
                    - ScriptColor enum name. (e.g. "DeepGray", "DeepBlue")
                    - String value shown in the Scriptable app. (e.g. "deep-gray", "deep-blue")

    include(name: String, icon: Any, color: Any):
        - Shortcut for include described above, automatically using name.toKebabCase() for the module name.

 */


scriptable {

    iCloudScriptableDirectory.set(file(environment("SCRIPTABLE_ICLOUD_PATH")))
    iCloudScriptableCacheDirectory.set(file(environment("SCRIPTABLE_DATA_PATH")))

    defaultMinifyScripts.set(true)
    defaultIcon.set(ScriptIcon.Desktop)
    defaultColor.set(ScriptColor.DeepGray)

    include("show-table-example")
    include("show-alert-example")

}