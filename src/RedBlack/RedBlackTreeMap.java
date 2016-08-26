package RedBlack;
import java.util.*;

// A Map ADT structure using a red-black tree, where keys must implement
// Comparable.
public class RedBlackTreeMap<TKey extends Comparable<TKey>, TValue> {
	
	public RedBlackTreeMap () {
		mRoot = null;
		mCount = 0;
	}
	// A Node class.
	private class Node {
 
		
		public Node(TKey key, TValue data, boolean isRed) {
			mKey = key;
			mValue = data;
			mIsRed = isRed;
			
			mLeft = NIL_NODE;
			mRight = NIL_NODE;
		}
		
		public void InOrder(ArrayList <TKey> a) {
			if (mLeft != NIL_NODE)
				mLeft.InOrder(a);
			a.add(mKey);
			if (mRight != NIL_NODE)
				mRight.InOrder(a);
		}
		
		@Override
		public String toString() {
			return "(" + mKey + ", " + mValue + ")";
		}
		private TKey mKey;
		private TValue mValue;
		private Node mParent;
		private Node mLeft;
		private Node mRight;
		private boolean mIsRed;
	}


	
	//////////////////// I give you these utility functions for free.
	
	// Get the # of keys in the tree.
	public int getCount() {
		return mCount;
	}
	
	// Finds the value associated with the given key.
	public TValue find(TKey key) {
		Node n = bstFind(key, mRoot); // find the Node containing the key if any
		if (n == null || n == NIL_NODE)
			throw new RuntimeException("Key not found");
		return n.mValue;
	}

	
	
	/////////////////// You must finish the rest of these methods.
	
	// Inserts a key/value pair into the tree, updating the red/black balance
	// of nodes as necessary. Starts with a normal BST insert, then adjusts.
	public void insert(TKey key, TValue data) {
		Node n = new Node(key, data, true); // nodes start red
		// normal BST insert; n will be placed into its initial position.
		// returns false if an existing node was updated (no rebalancing needed)
		boolean insertedNew = bstInsert(n, mRoot); 
		if (!insertedNew) {
			return;
		}
		
		else {
			cases(n);
		}
	}
	
	private void cases(Node n) {
		Node U = getUncle(n);
		Node G = getGrandparent(n);
		Node P = n.mParent;

		if (n == mRoot) {
			// case 1: new node is root.
			n.mIsRed = false;
			return;
		}
		// handle additional insert cases here. ONLY THIS. PREVIOUS CODE DONE!
		if (P != null) {
			if (!(n.mParent.mIsRed)) {
				//the parent is NOT red, so black
				return;
			}
			
			else if (P.mIsRed) {
				//case 3: P and U are red
				if (P.mIsRed && U.mIsRed) {
					P.mIsRed = false;
					U.mIsRed = false;
					G.mIsRed = true;
					cases(G);
					return;
				}
				//case 4: n is the lr or rl child of G
				if (G.mLeft.mRight == n ) {
					rotateLeft(P);
					rotateRight(G);
					return;
				}
				
				else if (G.mRight.mLeft == n) {
					rotateRight(P);
					rotateLeft(G);
					return;
				}
				//case 5: n is the ll or rr child of G
				if (G.mLeft.mLeft == n) {
					rotateRight(G);
					return;
				}
				else if (G.mRight.mRight == n) {
					rotateLeft(G);
					return;
				}
			}
				
		}
	}
	
	// Returns true if the given key is in the tree.
	public boolean containsKey(TKey key) {
		try {
			bstFind(key, mRoot);
			return true;
		}
		catch (RuntimeException e){
			System.err.print(key + " not present\n");
			return false;
		}
	}
	
	// Returns a list of all keys in the tree in sorted order.
	public List<TKey> keySet() {
		ArrayList <TKey> list = new ArrayList<TKey> ();
		mRoot.InOrder(list);
		return list;
	}
	
	
	// Retuns the Node containing the given key. Recursive. 
	private Node bstFind(TKey key, Node currentNode) throws RuntimeException {
		// TODO: write this method. Given a key to find and a node to start at,
		// proceed left/right from the current node until finding a node whose 
		// key is equal to the given key.
		if (currentNode == null || currentNode == NIL_NODE)
			throw new RuntimeException("Node not found");
		if (key.equals(currentNode.mKey))
			return currentNode;
		if (key.compareTo(currentNode.mKey) > 0)
			return bstFind (key, currentNode.mRight);
		else
			return bstFind(key, currentNode.mLeft);
	}
	
	
	//////////////// These functions are needed for insertion cases.
	
	// Gets the grandparent of n.
	private Node getGrandparent(Node n) { 
		if (n.mParent != null)
			return n.mParent.mParent;
		return null;
	}
	
	// Gets the uncle (parent's sibling) of n.
	private Node getUncle(Node n) {
		if (getGrandparent(n) == null){
			return null;
		}
		if (getGrandparent(n).mRight == n.mParent)
			return getGrandparent(n).mLeft;
		else 
			return getGrandparent(n).mRight;
	}
	
	// Rotate the tree right at the given node.
	private void rotateRight(Node n) {
		// TODO: do a single right rotation (AVL tree calls this a "ll" rotation)
		// at n.
		Node temp = n.mLeft;
		n.mLeft = temp.mRight;
		if (temp.mRight != null)
			temp.mRight.mParent = n;
		temp.mParent = n.mParent;
		if (n.mParent == null)
			mRoot = temp;
		else {
			if (n == (n.mParent.mRight))
				n.mParent.mRight = temp;
			else 
				n.mParent.mLeft = temp;
		}
		temp.mRight = n;
		n.mParent = temp;
	}
	
	// Rotate the tree left at the given node.
	private void rotateLeft(Node n) {
		// TODO: do a single left rotation (AVL tree calls this a "rr" rotation)
		// at n.
		Node temp = n.mRight;
		n.mRight = temp.mLeft;
		if (temp.mLeft != null)
			temp.mLeft.mParent = n;
		temp.mParent = n.mParent;
		if (n.mParent == null)
			mRoot = temp;
		else {
			if (n == (n.mParent.mLeft))
				n.mParent.mLeft = temp;
			else 
				n.mParent.mRight = temp;
			
		}
		temp.mLeft = n;
		n.mParent = temp;
	}
	
	
	// This method is used by insert. It is complete.
	// Inserts the key/value into the BST, and returns true if the key wasn't 
	// previously in the tree.
	private boolean bstInsert(Node newNode, Node currentNode) {
		if (mRoot == null) {
			// case 1
			mRoot = newNode;
			return true;
		}
		else{
			int compare = currentNode.mKey.compareTo(newNode.mKey);
			if (compare < 0) {
			// newNode is larger; go right.
				if (currentNode.mRight != NIL_NODE)
					return bstInsert(newNode, currentNode.mRight);
				else {
					currentNode.mRight = newNode;
					newNode.mParent = currentNode;
					mCount++;
					return true;
				}
			}
			else if (compare > 0) {
				if (currentNode.mLeft != NIL_NODE)
					return bstInsert(newNode, currentNode.mLeft);
				else {
					currentNode.mLeft = newNode;
					newNode.mParent = currentNode;
					mCount++;
					return true;
				}
			}
			else {
				// found a node with the given key; update value.
				currentNode.mValue = newNode.mValue;
				return false; // did NOT insert a new node.
			}
		}
	}
	
	// Rather than create a "blank" black Node for each NIL, we use one shared
	// node for all NIL leaves.
	private final Node NIL_NODE = new Node(null, null, false);
	private Node mRoot;
	private int mCount;
	
}
