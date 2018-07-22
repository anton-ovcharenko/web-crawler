_**Simple web crawler**_

**Task description:** 

See: https://agileengine.bitbucket.io/keFivpUlPMtzhfAy/

_Intro:_

`Imagine that you are writing a simple web crawler that locates a user-selected element on a web site with frequently 
changing information. You regularly face an issue that the crawler fails to find the element after minor page updates. 
After some analysis you decided to make your analyzer tolerant to minor website changes so that you don’t have to 
update the code every time.

It would be best to view the attached HTML page, imagining that you need to find the “Everything OK” button on every page.
`

_Requirements:_

`Write a program that analyzes HTML and finds a specific element, even after changes, using a set of extracted attributes. 
Samples of HTML pages (sample-0-origin) and 4 simple difference cases(sample-1-evil-gemini, sample-2-container-and-clone,
sample-3-the-escape, sample-4-the-mash) you can find in ./res/*. Please open the pages in browser to see what we mean 
by minor website changes. The target element that needs to be found by your program is the green “Everything OK” button. 
Any user can easily find this button visually, even when the site changes. Original contains a button with attribute 
id="make-everything-ok-button". This id is the only exact criteria, to find the target element in the input file.

The program must consume the original page to collect all the required information about the target element. 
Then the program should be able to find this element in diff-case HTML document that differs a bit from the original page. 
Original and diff-case HTML documents should be provided to the program in each run - no persistence is required.

Consider HTML samples, as regular XML files. No image/in-browser app analysis is needed. No CSS/JS analysis is needed 
(CSS/JS files are provided just for demo).`

**Execution:**

Compiled jar file can be found in folder ./bin/

Using default elementId ("make-everything-ok-button") in original file:
>java -jar web-crawler-VERSION-jar-with-dependencies.jar <input_origin_file_path> <input_other_sample_file_path> 

Or specifying elementId in original file:
>java -jar web-crawler-VERSION-jar-with-dependencies.jar <input_origin_file_path> <input_other_sample_file_path> <element_id_in_original_file>




