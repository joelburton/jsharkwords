Java-Based Multiple-Engine Hangman Game
=======================================

Created for a Java OO design patterns seminar I taught.

This includes engines for different hangman games:

- Normal hangman

- Evil hangman, which selects candidate answers trying to make it as difficult
  as possible for you to guess the word

- Nicely-Evil hangman, which is like evil hangman in keeping its options open,
  but prefers to find a word using your chosen letter

- Nice hangman, which changes candidate answers hoping to use you letter when
  possible

There is a both a console and a GUI frontend for this.

.. reminder

  to build dmg:
   /Library/Java/JavaVirtualMachines/jdk-14.0.2.jdk/Contents/Home/bin/jpackage --main-class sharkwords.frontends.Sharkwords --main-jar sharkwords.jar --input .
