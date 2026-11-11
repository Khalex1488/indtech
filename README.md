# Industrial Tech (Fabric 1.21.1)

Tin ore, generator, cables, electric furnace.

## Build the .jar
Requires JDK 21 and internet access.

    ./gradlew build        (Windows: gradlew.bat build)

The mod jar appears in `build/libs/indtech-0.1.0.jar` (not the `-sources` one).
Put it into `.minecraft/mods` together with Fabric API 0.102.1+1.21.1.

No local setup? Push this folder to a GitHub repository: the included workflow
(.github/workflows/build.yml) builds the jar and attaches it in Actions -> Artifacts.
