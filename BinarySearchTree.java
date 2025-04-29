class BinarySearchTree {
    private Node root;

    private class Node {
        String key;
        Object value;
        Node left;
        Node right;

        Node(String key, Object value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public void insert(String key, Object value) {
        root = insert(root, key, value);
    }

    private Node insert(Node node, String key, Object value) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
        }

        return node;
    }

    public Object search(String key) {
        return search(root, key);
    }

    private Object search(Node node, String key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return search(node.left, key);
        } else if (cmp > 0) {
            return search(node.right, key);
        } else {
            return node.value;
        }
    }

    public void print() {
        print(root);
    }

    private void print(Node node) {
        if (node != null) {
            print(node.left);
            System.out.println(node.key + " = " + node.value);
            print(node.right);
        }
    }
}
