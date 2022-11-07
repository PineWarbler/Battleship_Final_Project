# Battleship_Final_Project

GUI implementation of battleship board game.  Final project for COMP 220 F2022.

To-Do List (or preferred: create an issue on GitHub):
- [ ] task 1
- [x] task 2

#### How to get JavaFX working in IntelliJ with this project (assuming you've already installed JavaFX from [here](https://gluonhq.com/products/javafx/):

1. Add openjfx to libraries project setting

   File > Project Structure > Libraries > (plus to add Java library) > insert path to (...)/.openjfx/javafx-sdk-19/lib

2. Add VM run configuration:
   Run > Edit Configurations > (plus to add new configuration) > Application > Modify options > insert
   --module-path (...)/path/to/javafx/sdk --add-modules javafx.controls,javafx.fxml
> OK

### License

[MIT License](LICENSE.md)
