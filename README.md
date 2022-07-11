Requirements:
-----------------------------
* System: Mac/Windows (Untested on Linux builds)
* Java: 1.18.0
    * If this is your first-time installing Java, you may need to set up your System's recognition of it:
        * Setting up your PATH
            * https://www.java.com/en/download/help/path.xml
            * https://mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/
* Apache Maven Version Range: 3.3.9 - 3.8.1
* IDE: IntelliJ Recommended
	* Plugins: 'Lombok Plugin' (Integrated into IntelliJ as of 2020.3)
		* In IntelliJ Settings, must enable `Annotations Processors` or compilation will fail!
			* Hint: 
				* Ctrl/Command+Shift+A gets you the quick search
				* Do this, then search for `Annotations Processors` to get quick link
	* Windows Users - Git Bash in Terminal instead of Windows OS Terminal
		* Settings > Terminal
			* Shell Path: `"C:\Program Files\Git\bin\sh.exe" -li`
			* Assuming you have installed Git Bash to the default location on your machine


Building Project with Maven:
-----------------------------
* Step 1: Navigate to the folder that you cloned the Stack to
  * Example: cd /Users/foo/Documents/Clipboard-Health-Test-Automation
* Step 2: Run the following command to build up the project for use
  * Command: mvn clean install
* Note:
  * Maven & JVM configuration has been customized -- see `.mvn` in root dir of stack


Tests
-----------------------------

* Running Tests
  * Where?
    * Class Locations
      * See ‘Test/ui/UITest class to directly run as a java class

* Via IntelliJ
       * TestNG Plugin installed by default; run or debug methods & classes via the play button
* Via TestNG.xml
       * A test can also be run through TestNG.xml, simply right click on TestNG.xml file and run it
	
        




Additional features:

Healenium:
Healenium is an AI-powered self-healing tool for selenium-based automation framework. Before implementing the Healenium please config the healenium backend with the help of a Docker image.

Please follow the step to given here: https://github.com/healenium/healenium-backend

If the test is run as a maven build without running the docker image, it might through some error, but test will execute successfully.



JsoupHelper: 

JsoupHelper is a library to find elements in the DOM, and it is much faster than Selenium-based finding mechanism. Although, there is no Jsoup method used in the test, the class with name JsoupHelper can be seen in the project.


Extent Reports:

Extent report is widely used with Selenium for displaying the test reports, same has been integrated into our project. It is HTML file which can be seen in the root folder.

Log4j. SLF4J, Lombok:

For logging purposes, we have used these libraries, which is used by simply adding the Slf4j annotation in each class.

=========================================================================



Style Guide:
-----------------------------
* General Formatting:
	*Google's coding standards have been followed for source code writing in the Java
		* References:
			* https://github.com/google/google-java-format
			* https://plugins.jetbrains.com/plugin/8527-google-java-format
		* Except for:
			* We don't itemize imports -- wildcards allowed
			* We remove braces wherever syntactically correct to reduce clutter to its minimum :)

* Overall Syntax, Composition, Architectural Strategy
	* We follow the `Clean Code` principles set forth by Robert C. Martin (A.K.A. Uncle Bob)
		* Simplification
			* Class/Interface/Method/Etc.
				* Should be _VERY_ well named
					* Should be clear what it does without reading the contents, if possible
					* If code changes/ages, then we should fix it ;)
					* Directive: Having to comment a method for clarity, probably means a better name would help :)
				* Should do one thing very well
					* If it has multiple parts/phases then extract the handling, if possible
			* Architecture should be as re-usable and abstract as possible, favoring separation of concerns to allow independent evolution/maintenance
				* Caveat: Initial prototype violated this, but we evollve/maintain as the unknowns become known :)
