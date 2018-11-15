package Catalog;

import Catalog.UI.UI;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Catalog catalog = new Catalog(new File("C:\\Users\\me\\IdeaProjects\\MusicCatalog\\src\\Catalog\\Cache.txt"));
        UI ui = new UI(catalog);

        ui.menu();
    }
}