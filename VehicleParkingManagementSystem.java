// import java.time.LocalDateTime;
// import java.util.Scanner;

// class Vehicle {
//     String vehicleNumber;
//     String ownerName;
//     LocalDateTime entryTime;

//     Vehicle(String vehicleNumber, String ownerName) {
//         this.vehicleNumber = vehicleNumber;
//         this.ownerName = ownerName;
//         this.entryTime = LocalDateTime.now();
//     }

//     void display() {
//         System.out.println("Vehicle Number: " + vehicleNumber);
//         System.out.println("Owner Name: " + ownerName);
//         System.out.println("Entry Time: " + entryTime);
//         System.out.println("------------------------------");
//     }
// }

// // Node for Linked List
// class Node {
//     Vehicle vehicle;
//     Node next;

//     Node(Vehicle vehicle) {
//         this.vehicle = vehicle;
//         this.next = null;
//     }
// }

// // Linked List Management
// class VehicleList {
//     Node head;

//     void addVehicle(Vehicle v) {
//         Node newNode = new Node(v);
//         if (head == null) {
//             head = newNode;
//         } else {
//             Node temp = head;
//             while (temp.next != null) {
//                 temp = temp.next;
//             }
//             temp.next = newNode;
//         }
//         System.out.println("Vehicle parked successfully!");
//     }

//     void removeVehicle(String number) {
//         if (head == null) {
//             System.out.println("No vehicles parked.");
//             return;
//         }
//         if (head.vehicle.vehicleNumber.equals(number)) {
//             head = head.next;
//             System.out.println("Vehicle removed.");
//             return;
//         }
//         Node temp = head;
//         while (temp.next != null && !temp.next.vehicle.vehicleNumber.equals(number)) {
//             temp = temp.next;
//         }
//         if (temp.next == null) {
//             System.out.println("Vehicle not found.");
//         } else {
//             temp.next = temp.next.next;
//             System.out.println("Vehicle removed.");
//         }
//     }

//     void displayVehicles() {
//         if (head == null) {
//             System.out.println("No vehicles parked.");
//             return;
//         }
//         Node temp = head;
//         while (temp != null) {
//             temp.vehicle.display();
//             temp = temp.next;
//         }
//     }

//     Vehicle[] toArray() {
//         int count = 0;
//         Node temp = head;
//         while (temp != null) {
//             count++;
//             temp = temp.next;
//         }

//         Vehicle[] arr = new Vehicle[count];
//         temp = head;
//         int i = 0;
//         while (temp != null) {
//             arr[i++] = temp.vehicle;
//             temp = temp.next;
//         }
//         return arr;
//     }

//     void sortVehiclesByEntryTime() {
//         Vehicle[] arr = toArray();
//         mergeSort(arr, 0, arr.length - 1);

//         head = null;
//         for (Vehicle v : arr) {
//             addVehicle(v);
//         }
//         System.out.println("Vehicles sorted by entry time.");
//     }

//     void mergeSort(Vehicle[] arr, int left, int right) {
//         if (left < right) {
//             int mid = (left + right) / 2;
//             mergeSort(arr, left, mid);
//             mergeSort(arr, mid + 1, right);
//             merge(arr, left, mid, right);
//         }
//     }

//     void merge(Vehicle[] arr, int left, int mid, int right) {
//         int n1 = mid - left + 1;
//         int n2 = right - mid;

//         Vehicle[] L = new Vehicle[n1];
//         Vehicle[] R = new Vehicle[n2];

//         for (int i = 0; i < n1; i++)
//             L[i] = arr[left + i];
//         for (int j = 0; j < n2; j++)
//             R[j] = arr[mid + 1 + j];

//         int i = 0, j = 0, k = left;
//         while (i < n1 && j < n2) {
//             if (L[i].entryTime.isBefore(R[j].entryTime)) {
//                 arr[k++] = L[i++];
//             } else {
//                 arr[k++] = R[j++];
//             }
//         }
//         while (i < n1) arr[k++] = L[i++];
//         while (j < n2) arr[k++] = R[j++];
//     }

//     void binarySearch(String number) {
//         Vehicle[] arr = toArray();
//         mergeSort(arr, 0, arr.length - 1);
//         int result = binarySearchRecursive(arr, 0, arr.length - 1, number);
//         if (result != -1) {
//             System.out.println("Vehicle Found:");
//             arr[result].display();
//         } else {
//             System.out.println("Vehicle not found.");
//         }
//     }

//     int binarySearchRecursive(Vehicle[] arr, int low, int high, String number) {
//         if (low <= high) {
//             int mid = (low + high) / 2;
//             int cmp = arr[mid].vehicleNumber.compareTo(number);
//             if (cmp == 0) return mid;
//             else if (cmp < 0) return binarySearchRecursive(arr, mid + 1, high, number);
//             else return binarySearchRecursive(arr, low, mid - 1, number);
//         }
//         return -1;
//     }
// }

// // BST for fast search
// class BSTNode {
//     Vehicle vehicle;
//     BSTNode left, right;

//     BSTNode(Vehicle v) {
//         vehicle = v;
//         left = right = null;
//     }
// }

// class VehicleBST {
//     BSTNode root;

//     void insert(Vehicle v) {
//         root = insertRec(root, v);
//     }

//     BSTNode insertRec(BSTNode root, Vehicle v) {
//         if (root == null) return new BSTNode(v);
//         if (v.vehicleNumber.compareTo(root.vehicle.vehicleNumber) < 0)
//             root.left = insertRec(root.left, v);
//         else
//             root.right = insertRec(root.right, v);
//         return root;
//     }

//     void inorder(BSTNode root) {
//         if (root != null) {
//             inorder(root.left);
//             root.vehicle.display();
//             inorder(root.right);
//         }
//     }

//     void search(String number) {
//         BSTNode res = searchRec(root, number);
//         if (res != null) res.vehicle.display();
//         else System.out.println("Vehicle not found in BST.");
//     }

//     BSTNode searchRec(BSTNode root, String number) {
//         if (root == null || root.vehicle.vehicleNumber.equals(number)) return root;
//         if (number.compareTo(root.vehicle.vehicleNumber) < 0)
//             return searchRec(root.left, number);
//         else
//             return searchRec(root.right, number);
//     }
// }

// // Stack for managing last parked vehicles
// class VehicleStack {
//     Node top;

//     void push(Vehicle v) {
//         Node n = new Node(v);
//         n.next = top;
//         top = n;
//     }

//     Vehicle pop() {
//         if (top == null) {
//             System.out.println("Stack is empty");
//             return null;
//         }
//         Vehicle v = top.vehicle;
//         top = top.next;
//         return v;
//     }

//     void displayStack() {
//         Node temp = top;
//         while (temp != null) {
//             temp.vehicle.display();
//             temp = temp.next;
//         }
//     }
// }

// public class VehicleParkingManagementSystem {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         VehicleList list = new VehicleList();
//         VehicleBST bst = new VehicleBST();
//         VehicleStack stack = new VehicleStack();

//         while (true) {
//             System.out.println("\n==== Vehicle Parking Management System ====");
//             System.out.println("1. Park Vehicle");
//             System.out.println("2. Remove Vehicle");
//             System.out.println("3. Display All Vehicles");
//             System.out.println("4. Sort Vehicles by Entry Time");
//             System.out.println("5. Search Vehicle (Binary Search)");
//             System.out.println("6. Insert/Search in BST");
//             System.out.println("7. Stack: Last Parked Vehicles");
//             System.out.println("8. Exit");
//             System.out.print("Choose an option: ");
//             int choice = sc.nextInt();
//             sc.nextLine();

//             switch (choice) {
//                 case 1:
//                     System.out.print("Enter Vehicle Number: ");
//                     String num = sc.nextLine();
//                     System.out.print("Enter Owner Name: ");
//                     String name = sc.nextLine();
//                     Vehicle v = new Vehicle(num, name);
//                     list.addVehicle(v);
//                     bst.insert(v);
//                     stack.push(v);
//                     break;
//                 case 2:
//                     System.out.print("Enter Vehicle Number to Remove: ");
//                     String removeNum = sc.nextLine();
//                     list.removeVehicle(removeNum);
//                     break;
//                 case 3:
//                     list.displayVehicles();
//                     break;
//                     case 4:
//                     list.sortVehiclesByEntryTime();  // Sorts the vehicles by entry time
//                     System.out.println("Sorted Vehicles by Entry Time:");
//                     list.displayVehicles();  // Display the vehicles after sorting
//                     break;
                
//                 case 5:
//                     System.out.print("Enter Vehicle Number to Search: ");
//                     String searchNum = sc.nextLine();
//                     list.binarySearch(searchNum);
//                     break;
//                     case 6:
//                     System.out.print("Enter Vehicle Number to Insert/Search in BST: ");
//                     String searchBST = sc.nextLine();
                
//                     // Inserting the vehicle into BST
//                     Vehicle newVehicle = new Vehicle(searchBST, "Owner Name");
//                     bst.insert(newVehicle);  // Insert the new vehicle into the BST
                    
//                     System.out.println("Vehicle inserted into BST.");
                    
//                     // Searching for the vehicle in BST
//                     System.out.print("Enter Vehicle Number to Search in BST: ");
//                     String searchNumber = sc.nextLine();
//                     bst.search(searchNumber);  // Search for the vehicle in BST
//                     break;
                
//                 case 7:
//                     System.out.println("Stack of Recently Parked Vehicles:");
//                     stack.displayStack();
//                     break;
//                 case 8:
//                     System.out.println("Exiting...");
//                     return;
//                     case 9:
//     System.out.println("Vehicles in BST (Sorted by Number):");
//     bst.inorder(bst.root);
//     break;
   


                
//                 default:
//                     System.out.println("Invalid choice.");
//             }
//         }
//     }
// }
import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Vehicle class (Serializable for file persistence)
class Vehicle implements Serializable {
    String vehicleNumber;
    String ownerName;
    LocalDateTime entryTime;

    Vehicle(String vehicleNumber, String ownerName) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.entryTime = LocalDateTime.now();
    }

    void display() {
        System.out.println("Vehicle Number: " + vehicleNumber);
        System.out.println("Owner Name: " + ownerName);
        System.out.println("Entry Time: " + entryTime);
        System.out.println("------------------------------");
    }
}

// Node class for LinkedList/Stack
class Node {
    Vehicle vehicle;
    Node next;

    Node(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.next = null;
    }
}

// LinkedList management
class VehicleList {
    Node head;

    // File name for persistence
    final String FILE_NAME = "vehicles.dat";

    void addVehicle(Vehicle v) {
        Node newNode = new Node(v);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        System.out.println("Vehicle parked successfully!");
        saveToFile();
    }

    void removeVehicle(String number) {
        if (head == null) {
            System.out.println("No vehicles parked.");
            return;
        }
        if (head.vehicle.vehicleNumber.equals(number)) {
            head = head.next;
            System.out.println("Vehicle removed.");
            saveToFile();
            return;
        }
        Node temp = head;
        while (temp.next != null && !temp.next.vehicle.vehicleNumber.equals(number)) {
            temp = temp.next;
        }
        if (temp.next == null) {
            System.out.println("Vehicle not found.");
        } else {
            temp.next = temp.next.next;
            System.out.println("Vehicle removed.");
            saveToFile();
        }
    }

    void displayVehicles() {
        if (head == null) {
            System.out.println("No vehicles parked.");
            return;
        }
        Node temp = head;
        while (temp != null) {
            temp.vehicle.display();
            temp = temp.next;
        }
    }

    Vehicle[] toArray() {
        int count = 0;
        Node temp = head;
        while (temp != null) {
            count++;
            temp = temp.next;
        }

        Vehicle[] arr = new Vehicle[count];
        temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.vehicle;
            temp = temp.next;
        }
        return arr;
    }

    void sortVehiclesByEntryTime() {
        Vehicle[] arr = toArray();
        mergeSort(arr, 0, arr.length - 1);

        // Rebuild LinkedList after sorting
        head = null;
        for (Vehicle v : arr) {
            addVehicleNoSave(v); // Avoid multiple file writes
        }
        saveToFile();
        System.out.println("Vehicles sorted by entry time.");
    }

    void mergeSort(Vehicle[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    void merge(Vehicle[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Vehicle[] L = new Vehicle[n1];
        Vehicle[] R = new Vehicle[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].entryTime.isBefore(R[j].entryTime)) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    void binarySearch(String number) {
        Vehicle[] arr = toArray();
        mergeSort(arr, 0, arr.length - 1);
        int result = binarySearchRecursive(arr, 0, arr.length - 1, number);
        if (result != -1) {
            System.out.println("Vehicle Found:");
            arr[result].display();
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    int binarySearchRecursive(Vehicle[] arr, int low, int high, String number) {
        if (low <= high) {
            int mid = (low + high) / 2;
            int cmp = arr[mid].vehicleNumber.compareTo(number);
            if (cmp == 0) return mid;
            else if (cmp < 0) return binarySearchRecursive(arr, mid + 1, high, number);
            else return binarySearchRecursive(arr, low, mid - 1, number);
        }
        return -1;
    }

    // Save LinkedList to file
    void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            Node temp = head;
            LinkedList<Vehicle> list = new LinkedList<>();
            while (temp != null) {
                list.add(temp.vehicle);
                temp = temp.next;
            }
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load LinkedList from file
    void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            LinkedList<Vehicle> list = (LinkedList<Vehicle>) ois.readObject();
            for (Vehicle v : list) {
                addVehicleNoSave(v);
            }
        } catch (FileNotFoundException e) {
            // File not found on first run — ignore
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Helper to add vehicle without saving (used in sort and load)
    void addVehicleNoSave(Vehicle v) {
        Node newNode = new Node(v);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
    }
}

// BST for fast search
class BSTNode {
    Vehicle vehicle;
    BSTNode left, right;

    BSTNode(Vehicle v) {
        vehicle = v;
        left = right = null;
    }
}

class VehicleBST {
    BSTNode root;

    void insert(Vehicle v) {
        root = insertRec(root, v);
    }

    BSTNode insertRec(BSTNode root, Vehicle v) {
        if (root == null) return new BSTNode(v);
        if (v.vehicleNumber.compareTo(root.vehicle.vehicleNumber) < 0)
            root.left = insertRec(root.left, v);
        else
            root.right = insertRec(root.right, v);
        return root;
    }

    void inorder(BSTNode root) {
        if (root != null) {
            inorder(root.left);
            root.vehicle.display();
            inorder(root.right);
        }
    }

    void search(String number) {
        BSTNode res = searchRec(root, number);
        if (res != null) res.vehicle.display();
        else System.out.println("Vehicle not found in BST.");
    }

    BSTNode searchRec(BSTNode root, String number) {
        if (root == null || root.vehicle.vehicleNumber.equals(number)) return root;
        if (number.compareTo(root.vehicle.vehicleNumber) < 0)
            return searchRec(root.left, number);
        else
            return searchRec(root.right, number);
    }
}

// Stack for recent vehicles
class VehicleStack {
    Node top;

    void push(Vehicle v) {
        Node n = new Node(v);
        n.next = top;
        top = n;
    }

    void displayStack() {
        Node temp = top;
        while (temp != null) {
            temp.vehicle.display();
            temp = temp.next;
        }
    }
}

// Queue for FIFO parking order
class VehicleQueue {
    Queue<Vehicle> queue = new LinkedList<>();

    void enqueue(Vehicle v) {
        queue.add(v);
    }

    void displayQueue() {
        for (Vehicle v : queue) {
            v.display();
        }
    }
}

// Main Class
public class VehicleParkingManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VehicleList list = new VehicleList();
        VehicleBST bst = new VehicleBST();
        VehicleStack stack = new VehicleStack();
        VehicleQueue queue = new VehicleQueue();

        // Load existing vehicles
        list.loadFromFile();

        // Populate BST, Stack, Queue from loaded data
        Node temp = list.head;
        while (temp != null) {
            bst.insert(temp.vehicle);
            stack.push(temp.vehicle);
            queue.enqueue(temp.vehicle);
            temp = temp.next;
        }

        while (true) {
            System.out.println("\n==== Vehicle Parking Management System ====");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Display All Vehicles");
            System.out.println("4. Sort Vehicles by Entry Time");
            System.out.println("5. Search Vehicle (Binary Search)");
            System.out.println("6. Search Vehicle (BST)");
            System.out.println("7. Stack: Last Parked Vehicles");
            System.out.println("8. Queue: FIFO Parking Order");
            System.out.println("9. Display Vehicles in BST");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Vehicle Number: ");
                    String num = sc.nextLine();
                    System.out.print("Enter Owner Name: ");
                    String name = sc.nextLine();
                    Vehicle v = new Vehicle(num, name);
                    list.addVehicle(v);
                    bst.insert(v);
                    stack.push(v);
                    queue.enqueue(v);
                    break;
                case 2:
                    System.out.print("Enter Vehicle Number to Remove: ");
                    String removeNum = sc.nextLine();
                    list.removeVehicle(removeNum);
                    break;
                case 3:
                    list.displayVehicles();
                    break;
                case 4:
                    list.sortVehiclesByEntryTime();
                    list.displayVehicles();
                    break;
                case 5:
                    System.out.print("Enter Vehicle Number to Search: ");
                    String searchNum = sc.nextLine();
                    list.binarySearch(searchNum);
                    break;
                case 6:
                    System.out.print("Enter Vehicle Number to Search in BST: ");
                    String searchNumber = sc.nextLine();
                    bst.search(searchNumber);
                    break;
                case 7:
                    System.out.println("Stack of Recently Parked Vehicles:");
                    stack.displayStack();
                    break;
                case 8:
                    System.out.println("Queue of Vehicles (FIFO):");
                    queue.displayQueue();
                    break;
                case 9:
                    System.out.println("Vehicles in BST (Sorted by Number):");
                    bst.inorder(bst.root);
                    break;
                case 10:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
