The core module provides the foundation to traversing the UIAutomation element tree. It provides a number of functions to filter and navigate based on matched elements.

Mechanic selectors are much like selectors in frameworks such as jQuery or Dojo. They can be iterated over and also have a number of additional methods made available by other modules.

$()
The mechanic (shortcut to $ for convenience) forms the backbone to the framework. It's signature is:

$(selector, context = frontMostApp())
The 1st arg can take a number of forms. If a UIAElement or an array of UIAElements is passed into it, the matched elements will simply be the element or elements passed in. The 2nd arg (the context) is ignored in this case.

If a selector is passed as the 1st arg, the resulting selector is simply the selector originally passed in. The 2nd arg is ignored in this case. To better drive this home:

// ...
var sel1 = $(win);    // 'win' is a UIAWindow
var sel2 = $(sel1, someOtherWin);   // sel2 == sel1
If a string is passed as the selector, a number of scenarios occur. To match by a name/label, prefix the string passed with a '#' character. This is similar to other CSS-style selector frameworks:

// assume there's a button named 'submitButton'
var selForBtn = $('#submitButton');
// selForBtn now contains the button!
UIAElements can also be matched by type using strings, simply pass the name of the UIAElement subclass you wish to match on to create a selector with elements of that type:

var selForAllLabels = $('UIALabel');
// selForAllLabels contains all the app's labels!

https://github.com/jaykz52/mechanic/wiki
https://github.com/jaykz52/mechanic/blob/master/README.md
