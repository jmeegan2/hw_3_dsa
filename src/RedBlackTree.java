import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RedBlackTree {
    /**
     * Values for node color
     */
    public enum Color {RED, BLACK}

    // private variable for null node
    private final Node nil = new Node(-1);

    // class to represent tree node
    private class Node{
        int value = -1;
        Color color = Color.BLACK;
        Node left = nil, right = nil, parent = nil;
        // parameterized constructor
        Node(int val){
            this.value = val;
        }
    }

    // root node initialized to nil
    private Node root = nil;

    /**
     * Empty constructor initializes root to nil
     */
    public RedBlackTree() {
        root = nil;
    }

    /**
     * Prints tree in in-order fashion from root
     */
    public void printInorder() {
        printTree(root);
        System.out.println();
    }

    /*
     * Prints nodes in in-order fashion from node z
     * @param z starting node
     */
    private void printTree(Node z) {
        if (z == nil) {
            return;
        }
        printTree(z.left);
        System.out.print(z.value + " (" + z.color + ") ");
        printTree(z.right);
    }

    /**
     * Inserts node with value v into the tree
     * @param v value to insert
     */
    public void insertByValue(int v) {
        Node inNode = new Node(v);
        insert(inNode);
    }

    /*
     * Inserts node in the tree
     * @param node node to insert
     */
    private void insert(Node z) {
        Node temp = root;
        if (root == nil) { // empty tree
            root = z;
            z.color = Color.BLACK;
            z.parent = nil;
        } else {
            z.color = Color.RED;
            while (true) {
                if (z.value < temp.value) { // node z belongs in left subtree
                    if (temp.left == nil) { // insert node z
                        temp.left = z;
                        z.parent = temp;
                        break;
                    } else { // move left
                        temp = temp.left;
                    }
                }else if (z.value == temp.value) { // node z already in the tree
                    return;
                } else { // (node.value > temp.value) node z belongs in right subtree
                    if (temp.right == nil) { // insert node z
                        temp.right = z;
                        z.parent = temp;
                        break;
                    } else { // move right
                        temp = temp.right;
                    }
                }
            }
            // fix any violations
            fixupInsert(z);
        }
    }

    // fixes any violations due to insert
    private void fixupInsert(Node z) {
        while (z.parent.color == Color.RED) { // while there is a red-red violation
            Node uncle = nil;
            if (z.parent == z.parent.parent.left) { // parent is left child
                uncle = z.parent.parent.right;

                if (uncle != nil && uncle.color == Color.RED) { // case 1: uncle is RED
                    // re-color parent, grandparent and uncle
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    // move up the tree
                    z = z.parent.parent;
                    continue;
                }
                // else, uncle is BLACK
                if (z == z.parent.right) { // case 2: z, p, g form a triangle
                    // rotate z's parent in opposite direction of z
                    z = z.parent;
                    // double rotation needed
                    leftRotate(z);
                }
                // if above code hasn't executed,
                // case 3: z, p, g form a line
                z.parent.color = Color.BLACK;
                z.parent.parent.color = Color.RED;
                // single rotation needed
                rightRotate(z.parent.parent);
            } else { // parent is right child
                uncle = z.parent.parent.left;
                if (uncle != nil && uncle.color == Color.RED) { // case 1: uncle is RED
                    // re-color parent, grandparent and uncl
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    // move up the tree
                    z = z.parent.parent;
                    continue;
                }
                // else, uncle is BLACK
                if (z == z.parent.left) { // case 2: z, p, g form a triangle
                    // rotate z's parent in opposite direction of z
                    z = z.parent;
                    // double rotation needed
                    rightRotate(z);
                }
                // if above code hasn't executed,
                // case 3: z, p, g form a line
                z.parent.color = Color.BLACK;
                z.parent.parent.color = Color.RED;
                // single rotation needed
                leftRotate(z.parent.parent);
            }
        }
        // case 0: z is root, color it BLACK
        root.color = Color.BLACK;
    }

    // left rotate the given node z
    private void leftRotate(Node z) {
        if (z.parent != nil) { // somewhere in the tree
            if (z == z.parent.left) { // node is left child
                z.parent.left = z.right;
            } else { // node is right child
                z.parent.right = z.right;
            }
            z.right.parent = z.parent;
            z.parent = z.right;
            if (z.right.left != nil) {
                z.right.left.parent = z;
            }
            z.right = z.right.left;
            z.parent.left = z;
        } else { // rotating root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    // right rotate the given node
    private void rightRotate(Node z) {
        if (z.parent != nil) { // somewhere in the tree
            if (z == z.parent.left) { // node is left child
                z.parent.left = z.left;
            } else { // node is right child
                z.parent.right = z.left;
            }

            z.left.parent = z.parent;
            z.parent = z.left;
            if (z.left.right != nil) {
                z.left.right.parent = z;
            }
            z.left = z.left.right;
            z.parent.right = z;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    /*
     * Search for a node with given value
     * @param key value to look for
     * @return Node with a given value, null if not found
     */
    private Node findByValue(int key) {
        Node temp = root;
        while (true) {
            if (key < temp.value) { // node in left subtree
                if (temp.left == nil) { // not found
                    return null;
                } else { // move left
                    temp = temp.left;
                }
            }else if (key == temp.value) { // bingo!
                return temp;
            } else { // (node.value > temp.value) node in right subtree
                if (temp.right == nil) { // not found
                    return null;
                } else { // move right
                    temp = temp.right;
                }
            }
        }
    }

    /**
     * Searches for a node with value key
     * @param key value to search for
     * @return true if found, false otherwise
     */
    public boolean searchForKey(int key) {
        return findByValue(key) != null;
    }

    // places node v into target node u's position
    private void transplant(Node u, Node v){
        if(u.parent == nil){ // u is root
            root = v;
        }else if(u == u.parent.left){ // u is left child
            u.parent.left = v;
        }else // u is right child
            u.parent.right = v;
        // assign v's parent unconditionally
        v.parent = u.parent;
    }
    // returns node with minimum value in a subtree with root in z
    private Node treeMinimum(Node z){
        // go as far left as possible
        while(z.left!=nil){
            z = z.left;
        }
        return z;
    }

    // deletes node z from the tree
    // returns true if successful, false otherwise
    private boolean delete(Node z){
        Node y = z; // reference to z, might cause violations
        Color y_original_color = y.color;
        Node x; // will move into y's position, might cause violations

        // z has fewer than 2 children, 
        // thus it will be removed
        if(z.left == nil){ // z only has right child
            x = z.right;
            // put right child into z's position
            transplant(z, z.right);
        }else if(z.right == nil){ // z only has left child
            x = z.left;
            // put left child into z's position
            transplant(z, z.left);
        }else{ // z has 2 children
            // y is z's successor
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            // case when z is y's original parent
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Color.BLACK){
            fixupDelete(x);
        }
        return true;
    }

    // fixes any violations due to delete
    private void fixupDelete(Node x){
        // x points to a non-root black node
        while(x!=root && x.color == Color.BLACK){
            if(x == x.parent.left){ // x is left child
                Node w = x.parent.right; // x's sibling 
                if(w.color == Color.RED){ // case 1: w is RED
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                // w is BLACK
                // case 2: both w's children BLACK
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                // case 3: w's left child is RED and right child is BLACK
                else if(w.right.color == Color.BLACK){
                    w.left.color = Color.BLACK;
                    w.color = Color.RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                // case 4: w's left child is BLACK and right child is RED
                if(w.right.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }else{ // x is right child
                Node w = x.parent.left; // x's sibling
                if(w.color == Color.RED){ // case 1: w is RED
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                // w is BLACK
                // case 2: both w's children are BLACK
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                // case 3: w's left child is BLACK and right child is RED
                else if(w.left.color == Color.BLACK){
                    w.right.color = Color.BLACK;
                    w.color = Color.RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                // case 4: w's left child is RED and right child is BLACK
                if(w.left.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }

//    /**
//     * Deletes node with value key from the tree
//     * @param key value to remove
//     */
//    public void deleteByValue(int key) {
//        Node delNode = findByValue(key);
//        if(delNode == null) {
//            System.out.println("\t" + key + " not present. Abort delete.");
//            return;
//        }
//        delete(delNode);
//        System.out.println("\t" + key + " removed successfully.");
//    }

    public static void main(String[] args) throws IOException {

        //Random scanner class I just implemented
        Path filePath = Paths.get("src/positive_integers.txt");
        Scanner scanner = new Scanner(filePath);
        List<Integer> integers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                integers.add(scanner.nextInt());
            } else {
                scanner.next();
            }

        }


        System.out.println(" - means Red \n + means black");

    //END
        RedBlackTree rbt = new RedBlackTree();
        System.out.println(integers);
        rbt.insertByValue(1);
        rbt.insertByValue(2);
        rbt.insertByValue(3);
        rbt.insertByValue(4);
        rbt.insertByValue(5);
        rbt.insertByValue(6);
        rbt.insertByValue(7);
        rbt.insertByValue(8);
        rbt.insertByValue(9);
        rbt.insertByValue(10);
        rbt.insertByValue(11);
        rbt.insertByValue(12);
        System.out.print("\t");
        rbt.printInorder();


    }

}