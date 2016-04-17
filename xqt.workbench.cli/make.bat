REM d:

REM cd D:\Projects\PhD\Src\SciQuest\xqt.workbench.cli
DEL /F /S /Q /A "logs\*"
DEL /F /S /Q /A "temp\*"
mvn clean package assembly:single