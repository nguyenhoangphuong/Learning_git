package com.misfit.ta.gui;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewNode {

    // fields
    public String type;
    public String address;
    public Point[] frame;
    public ViewNode layer;
    public HashMap<String, String> properties;
    public ViewNode mom;
    public ArrayList<ViewNode> children;
    private int spacesBefore; // for attaching into ViewNode "tree"

    // methods
    public ViewNode() {
        this.type = "";
        this.address = "";
        this.frame = null;
        this.layer = null;
        this.properties = new HashMap<String, String>();
        this.mom = null;
        this.children = new ArrayList<ViewNode>();
        this.spacesBefore = -1;
    }

    protected static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));

        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            /* Instead of using default, pass in a decoder. */
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    protected static ViewNode parseALine(String line) {
        ViewNode node = new ViewNode();

        node.spacesBefore = line.indexOf("<");

        line = line.replace("<", "");
        line = line.replace(">", "");

        // this makes the function not cover 100%
        String[] elements = line.split(";");
        String[] subStrings;

        for (int i = 0; i < elements.length; i++) {
            elements[i] = elements[i].trim();

            if (i == 0) { // first one is type
                // string will be something like this:
                // UIButtonLabel: 0xc276e70
                subStrings = elements[i].split(": ");
                node.type = subStrings[0];
                node.address = subStrings[1];
            } else if (i == elements.length - 1) { // last one is layer
                node.layer = new ViewNode();

                // string will be something like this:
                // layer = CALayer: 0xc275120
                // therefore, we split them as below
                subStrings = elements[i].split(" = ");
                subStrings = subStrings[1].split(": ");

                node.layer.type = subStrings[0];
                node.layer.address = subStrings[1];
            } else if (elements[i].indexOf("=") >= 0) {
                // string will be something like this:
                // text = 'LOG IN'
                elements[i].replace("'", "");
                subStrings = elements[i].split(" = ");

                // TODO: remove this check after having found a way to split
                // effectively
                if (!subStrings[0].toLowerCase().equals("frame")) {
                    node.properties.put(subStrings[0], subStrings[1]);
                }
            }
        }

        return node;
    }

    protected static ArrayList<ViewNode> parseInput(String input) {
        String[] lines = input.split("\\r?\\n");
        ArrayList<ViewNode> nodes = new ArrayList<ViewNode>();

        for (int i = 0; i < lines.length; i++) {
            nodes.add(parseALine(lines[i]));
        }

        return nodes;
    }

    public static ViewNode createViewNodeTree(String input) {
        ArrayList<ViewNode> nodes = parseInput(input);
        int i = 0;
        ViewNode tree = nodes.get(i);

        for (i++; i < nodes.size(); i++) {
            addViewNode(nodes.get(i - 1), nodes.get(i));
        }

        return tree;
    }

    /**
     * Decide node b's level to node a
     * 
     * @param a
     *            a node on array
     * @param b
     *            a's neighbor on array
     */
    protected static void addViewNode(ViewNode a, ViewNode b) {
        if (a.spacesBefore < b.spacesBefore) {
            a.children.add(b);
            b.mom = a;
        } else if (a.spacesBefore == b.spacesBefore) {
            if (a.mom != null) {
                a.mom.children.add(b);
                b.mom = a.mom;
            }
        } else {
            if (a.mom != null) {
                addViewNode(a.mom, b);
            }
        }
    }

    public static void printViewNodeTree(ViewNode tree) {
        for (int i = 0; i < tree.spacesBefore; i++) {
            System.out.print(" ");
        }

        if (tree.mom != null) {
            System.out.println(tree.address + " of " + tree.mom.address);
        } else {
            System.out.println(tree.address);
        }

        for (int i = 0; i < tree.children.size(); i++) {
            printViewNodeTree(tree.children.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        ViewNode node = new ViewNode();
        String input = readFile("/Users/tung/Downloads/views.log");
        node = createViewNodeTree(input);
        // System.out.println("Done");
        printViewNodeTree(node);
    }
}
