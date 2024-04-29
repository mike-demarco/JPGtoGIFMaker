@echo off
setlocal enabledelayedexpansion

rem Set the path to the JPGtoGIFConverter application
set JPGtoGIFConverter=out\artifacts\gifMaker_jar\gifMaker.jar

rem Set the delay, width, and height parameters
set delay=1000
set width=1000
set height=562

rem Set the output folder path
set outputFolder=\gifs

rem Create the output folder if it doesn't exist
if not exist "%outputFolder%" mkdir "%outputFolder%"

rem Initialize counter
set counter=1

rem Iterate over subdirectories recursively
for /f %%d in ('dir /b /s /ad') do (
	rem Check if the subfolder contains JPG files
	dir /b "%%d\*.JPG"
	echo Found JPG files in folder: %%d
	if not errorlevel 1 (
        rem Get the output file name with sequential number
        set "outputFile=%outputFolder%\!counter!.gif"
        rem Increment counter
        set /a counter+=1

		rem Execute the JPGtoGIFConverter application with arguments
		echo %JPGtoGIFConverter% "%%d" "!outputFile!" %delay% %width% %height%
		java -jar %JPGtoGIFConverter% "%%d" "!outputFile!" %delay% %width% %height%
	)
)

pause
