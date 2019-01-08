
package DataStructures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LinkedBinarySearchTree class
 * based on implementation in Lewis & Chase textbook
 * Note that this is a very basic search tree that 
 * does NOTHING to maintain tree balance!
 * @author clatulip
 */
public class LinkedBinarySearchTree<T extends Comparable> extends LinkedBinaryTree implements BinarySearchTreeADT<T> {

    public LinkedBinarySearchTree() {
        super();
    }

    public LinkedBinarySearchTree(T element) {
        super(element);


    }

    @Override
    public void addElement(T element) {
        //Comparable<T> comparableElement = (Comparable<T>) element;

       
        if (isEmpty()) {
            root = new BTNode<T>(element);
        } else {

            if (element.compareTo(root.getElement()) < 0) {
                // element needs to go in left sub-tree
                if (root.getLeftChild() == null) {
                    this.root.setLeftChild(new BTNode<T>(element));
                } else {
                    addElement(element, root.getLeftChild());
                }
            } else {
                // element needs to go in right sub-tree
                if (root.getRightChild() == null) {
                    this.root.setRightChild(new BTNode<T>(element));
                } else {
                    addElement(element, root.getRightChild());
                }
            }
        }
        modCount++;

    }

    private void addElement(T element, BTNode<T> node) {
        //Comparable<T> comparableElement = (Comparable<T>) element;
        if (element.compareTo(node.getElement()) < 0) {
            // element needs to go in left sub-tree
            if (node.getLeftChild() == null) {
                node.setLeftChild(new BTNode<T>(element));
            } else {
                addElement(element, node.getLeftChild());
            }
        } else {
            // element needs to go in right sub-tree
            if (node.getRightChild() == null) {
                node.setRightChild(new BTNode<T>(element));
            } else {
                addElement(element, node.getRightChild());
            }
        }

    }

    /**
     * If the target element is in the tree, remove the first matching instance
     * found. Throws an exception if targetElement isn't comparable or if 
     * the element isn't found in a tree. Assume that the contains() method
     * has been called first to ensure the element is in the tree
     * @param targetElement the element we are looking to find and remove
     * @return the element found and removed
     * @throws NonComparableElementException
     * @throws ElementNotFoundException 
     */
    @Override
    public T removeElement(T targetElement) throws ElementNotFoundException {

        T result = null;

        if (isEmpty()) {
            throw new ElementNotFoundException("LinkedBinarySearchTree");
        } else {
            BTNode<T> parent = null;
            if (targetElement.equals(root.getElement())) {
                result = (T)(root.getElement());
                BTNode<T> temp = replacement(root);
                if (temp == null) {
                    root = null;
                } else {
                    root.setElement(temp.getElement());
                    root.setRightChild(temp.getRightChild());
                    root.setLeftChild(temp.getLeftChild());
                }
                modCount--;
                    
            } else {
                parent = root;
                if (targetElement.compareTo(root.getElement()) < 0) {
                    result = (T) removeElement(targetElement, root.getLeftChild(), parent);
                } else {
                    result = (T) removeElement(targetElement, root.getRightChild(), parent);
                }
            }
        }
        
        
        return result;
    }
    
    /**
     * Private helper method to recursively search sub-trees for 
     * element removal
     * @param targetElement the element to be searched for
     * @param node sub-tree
     * @param parent 
     * @return element if found, null otherwise
     * @throws ElementNotFoundException 
     */
    private T removeElement(T targetElement, BTNode<T> node, BTNode<T> parent) throws ElementNotFoundException {
        T result = null;
        
        if (node == null) {
            throw new ElementNotFoundException("LinkedBinarySearchTree");
        } else {
            if (targetElement.equals(node.getElement())) {
                result = node.getElement();
                BTNode<T> temp = replacement(node);
                if (parent.getRightChild() == node) {
                    parent.setRightChild(temp);
                } else {
                    parent.setLeftChild(temp);
                }
                modCount--;
            } else {
                parent = node;
                if (targetElement.compareTo(node.getElement()) < 0) {
                    result = removeElement(targetElement, node.getLeftChild(), parent);
                } else {
                    result = removeElement(targetElement, node.getRightChild(), parent);
                }
            }
        }
            
        
        return result;
    }

    /**
     * Remove all occurrences of a target element from the tree
     * Throw an exception if element is not comparable, or if element
     * is not found
     * @param targetElement the element to be searched for
     * @throws ElementNotFoundException
     * @throws NonComparableElementException 
     */
    @Override
    public void removeAllOccurrences(T targetElement) throws ElementNotFoundException {
        removeElement(targetElement);
        
        try {
            while (contains(targetElement)) {
                removeElement(targetElement);
            }
        } catch (Exception ElementNotFoundException) {
            // don't need to do anything, eventually we will stop finding items
        }
        
    }
    
    /**
     * private helper method to move a node up in the tree when a node is removed
     * @param node to be removed
     * @return node to take the place of the one removed
     */
    private BTNode<T> replacement(BTNode<T> node) {
        BTNode<T> result = null;
        
        if ((node.getLeftChild() == null) && (node.getRightChild() == null)) {
            result = null;
        }  else if ((node.getLeftChild() != null) && (node.getRightChild() == null)) {
            result = node.getLeftChild();
        } else if ((node.getLeftChild() == null) && (node.getRightChild() != null)) {
            result = node.getRightChild();
        } else {
            BTNode<T> current = node.getRightChild();
            BTNode<T> parent =  node;
            
            while (current.getLeftChild() != null) {
                parent = current;
                current = current.getLeftChild();
            }
            
            current.setLeftChild(node.getLeftChild());
            if (node.getRightChild() != current) {
                parent.setLeftChild(current.getRightChild());
                current.setRightChild(node.getRightChild());
            }
            
            result = current;
        }
        return result;
    }

    /**
     * Removes (after removing) the smallest element from the tree
     * @return the smallest element 
     * @throws EmptyCollectionException 
     */
    @Override
    public T removeMin() throws EmptyCollectionException {
        T result = null;
        
        if (isEmpty()) {
            throw new EmptyCollectionException("LinkedBinarySearchTree");
        } else {
            if (root.getLeftChild() == null) {
                result = (T)(root.getElement());
                root = root.getRightChild();
            } else {
                BTNode<T> parent = root;
                BTNode<T> current = root.getLeftChild();
                while (current.getLeftChild() != null) {
                    parent = current;
                    current = current.getLeftChild();
                }
                result = current.getElement();
                parent.setLeftChild(current.getRightChild());
            }
            
            modCount--;
        }
        
        return result;
    }


    /**
     * Finds and returns (without removing) the smallest element from the tree
     * @return the smallest element in the tree
     * @throws EmptyCollectionException 
     */
    @Override
    public T findMin() throws EmptyCollectionException {
        T result = null;
        
        if (isEmpty()) {
            throw new EmptyCollectionException("LinkedBinarySearchTree");
        } else {
            if (root.getLeftChild() == null) {
                result = (T)(root.getElement());
            } else {
                BTNode<T> parent = root;
                BTNode<T> current = root.getLeftChild();
                while (current.getLeftChild() != null) {
                    parent = current;
                    current = current.getLeftChild();
                }
                result = current.getElement();
            }
        }
        
        return result;
    }

    @Override
    public T removeMax() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // TODO as bonus
    }
    
    /**
     * A method that does a q-order traversal of the tree, creating
 a list of elements from the root down, with empty nodes specified by null
     * @return a list of elements in the tree in level order
     */
    public ArrayList<T> getTreeAsList() {
        
        ArrayList<T> tempList = new ArrayList<>();
        Queue<BTNode<T>> q  = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
         BTNode<T> node = q.poll();
         if (node != null) {
            tempList.add(node.getElement());
            q.add(node.getLeftChild());
            q.add(node.getRightChild());
         } else {
            tempList.add((T) "null");
         }
        }
        
        return tempList;
        

    }
    
      @Override
      public T findMax() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // TODO as bonus
      }

    @Override
    public String toString() {
        ArrayList<T> list = getTreeAsList();
        return "LinkedBinarySearchTree{" + list.toString() + '}';
    }


}
