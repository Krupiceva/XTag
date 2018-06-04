# XTag
An Application for manual Annotating and Tagging images

This application is developed as a part of my bachelor's final work and in cooperation with <a href="https://www.telegra-europe.com/">Telegra d.o.o!</a>
where I work as a student. 

<h2> Version releases </h2>
*XTag v2.0.0 - in progress <br>
*You need middle button on mouse to use app or enable "middle button" on trackpad
<ul>
  <li>Reduced edge rectangles</li>
  <li>New window for annotations configuration:<br>
  <ul>
    <li>Rectangle fill color for every class</li>
    <li>Rectangle border color for every class</li>
    <li>Flags color</li>
    <li>Default class</li>
    <li>Changing classes order with drag and drop</li>
  </ul>
  </li>
  <li>New visualization:<br>
    <ul>
      <li>Each class has its own color, the user can define it, the opacity can also be defined</li>
      <li>Defines color for all classes, so it is default</li>
      <li>User can define border color for all classes</li>
      <li>Each flag has a different border color</li>
      <li>Added a legend for the color of the flags</li>
      <li>The class color legend is in the table with anotations that are in the current image</li>
    </ul>
  </li>
  <li>Automatic save:<br>
    <ul>
      <li>Every change is automatically saved (in other words you should not use save button anymore)</li>
      <li>An image that hasn't have annotations so far is automatic forwarded to list of images with annotations when user draw new rectangle and it is shown in preview window, and if you press next (D), it is moved to the first image in the list of images without annotations</li>
      <li>When we click the previous button (A) in the list of images without annotations, it moves us to the last image in the list of images with annotations</li>
      <li>If all anotations are deleted from the image, the image returns to the list of images without anotations</li>
    </ul>
  </li>
  <li>On the first screen is added statistics of how many annotations there are in dataset (bar chart with every classes separately)</li>
  <li>Color annotatin:<br>
    <ul>
      <li>On every annotation user can add color</li>
      <li>Keyboard shortcuts: SHIFT+number (First 9 colors can have shortcuts)</li>
    </ul>
  </li>
  <li>Total number of annotations in dataset is added</li>
  <li>New Zoom:<br>
    <ul>
      <li>Automatically fit image to screen size</li>
      <li>Moving in zoomed in image with middle mouse button</li>
    </ul>
  </li>
  <li>Edit mode:<br>
    <ul>
      <li>When user is in edit mode, moving through the anotation list opens edit windows for that annotation</li>
      <li>Only the selected annotation is displayed and others are invisible</li>
      <li>With right mouse button pressed on image, user display all annotations</li>
    </ul>
  </li>
  <li>Undo/Redo functionality: IN PROGRESS</li>
</ul>

*XTag v1.3
<ul>
  <li>Delete the image through the application added</li>
  <li>Open image and xml location trough application added</li>
  <li>Added 3 list of images: images without annotations, images with annotations and verified images</li>
  <li>Removed radiobuttons from anotations list because of javafx8 bug</li>
  <li>Added lock aspect ratio to rectangles</li>
  <li>Class "unknow" added if not selected any other classes</li>
  <li>New dialog for importing datasets</li>
  <li>New dataset annotations</li>
  <li>Dataset can be open on double click</li>
</ul>    
*XTag v1.2
<ul>
  <li>Added new functionality for exporting frames as images from video</li>
</ul>
*XTag v1.1
<ul>
  <li>Keyboard shortcuts added (A, D, Q, W, E, CTRL + S)</li>
  <li>Double click open Edit Annotate Dialog now</li>
  <li>Minor buf fixes</li>
</ul>
