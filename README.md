# WE GET THESE 100S

Since we've learned how to test in Java, we will probably be required to write some tests in JUnit, so I also set up that.

You should open this on VSCode codespaces, which you should be able to run the code itself directly. You can also run and see the tests.

These code were taking from last week's lab sheet, but we'll change them later.

I've also created two different codespaces for us to use, so that it doesn't get mixed up.

To open the GUI, add port 6080 under the "Ports" section and open it in the browser.

## Core task
- Your simulation should have at least five different kinds of acting species. At least two of these should be predators (they eat another species), and at least two of them should not be predators (they may eat plants). Plants can either be assumed to always be available (as in the original project), or they can be simulated (see below).
- At least two predators should compete for the same food source.
- Some or all of the species should distinguish male and female individuals. For these, the creatures can only propagate when a male and female individual meet. (“Meet” means they need to be within a specified distance to each other, for example in a neighbouring cell.) You will need to experiment with the parameters for breeding probability to create a stable population.
- You should keep track of the time of day. At least some creatures should exhibit different behaviour at some time of the day (for example: they may sleep at night and not move during that time).

## Challenge tasks
- Add plants. Plants grow at a given rate, but they do not move. Some creatures eat plants. They will die if they do not find their food plant.
- Add weather. Weather can change, and it influences the behaviour of some simulated aspects. For example, grass may not grow without rain, or predators cannot see well in fog.
- Add disease. Some animals are occasionally infected. Infection can spread to other animals when they meet.

## Extra work -- just for fun
- You can extend the GUI (the graphical user interface) itself if you like, but no marks will be awarded for this work. If you do this – that’s good, but it is purely for fun and for your own practice.

## The report

The report should be no more than four pages long, and should include:
- The names of both students who worked on the submission.
- A description of your simulation, including the types of species that you are simulating, their behaviour and interactions.
- A list and description of all extension tasks you have implemented.
- Known bugs or problems (Note: for a bug in your code that you document yourself, you may not lose many marks – maybe none, if it is in a challenge task. For bugs that we find that you did not document you will probably lose marks.)

P.S. We can write our report in LaTeX, which I can create another repo for (I already kinda have a template from the last coursework) -- or we could just use Google Docs.