import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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

class Node implements Serializable {
    Vehicle vehicle;
    Node next;

    Node(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.next = null;
    }
}

class VehicleList implements Serializable {
    transient Node head;

    void addVehicle(Vehicle v) {
        Node newNode = new Node(v);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        System.out.println("Vehicle parked successfully!");
    }

    void removeVehicle(String number) {
        if (head == null) {
            System.out.println("No vehicles parked.");
            return;
        }
        if (head.vehicle.vehicleNumber.equals(number)) {
            head = head.next;
            System.out.println("Vehicle removed.");
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
        List<Vehicle> vehicleList = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            vehicleList.add(temp.vehicle);
            temp = temp.next;
        }
        return vehicleList.toArray(new Vehicle[0]);
    }

    void sortVehiclesByEntryTime() {
        Vehicle[] arr = toArray();
        mergeSort(arr, 0, arr.length - 1);
        head = null;
        for (Vehicle v : arr) {
            addVehicle(v);
        }
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
        Vehicle[] leftArr = Arrays.copyOfRange(arr, left, mid + 1);
        Vehicle[] rightArr = Arrays.copyOfRange(arr, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i].entryTime.isBefore(rightArr[j].entryTime)) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        while (i < leftArr.length) arr[k++] = leftArr[i++];
        while (j < rightArr.length) arr[k++] = rightArr[j++];
    }

    void binarySearch(String number) {
        Vehicle[] arr = toArray();
        Arrays.sort(arr, Comparator.comparing(v -> v.vehicleNumber));
        int index = Arrays.binarySearch(arr, new Vehicle(number, null), Comparator.comparing(v -> v.vehicleNumber));
        if (index >= 0) arr[index].display();
        else System.out.println("Vehicle not found.");
    }

    void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            List<Vehicle> vehicles = new ArrayList<>();
            Node temp = head;
            while (temp != null) {
                vehicles.add(temp.vehicle);
                temp = temp.next;
            }
            oos.writeObject(vehicles);
        } catch (IOException e) {
            System.out.println("Error saving vehicles: " + e.getMessage());
        }
    }

    void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Vehicle> vehicles = (List<Vehicle>) ois.readObject();
            for (Vehicle v : vehicles) addVehicle(v);
        } catch (Exception e) {
            System.out.println("No previous data found or error reading file.");
        }
    }
}

class BSTNode implements Serializable {
    Vehicle vehicle;
    BSTNode left, right;

    BSTNode(Vehicle v) {
        vehicle = v;
        left = right = null;
    }
}

class VehicleBST implements Serializable {
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

    void saveToFile(String filename) {
        List<Vehicle> list = new ArrayList<>();
        collectVehicles(root, list);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving BST: " + e.getMessage());
        }
    }

    void collectVehicles(BSTNode root, List<Vehicle> list) {
        if (root != null) {
            collectVehicles(root.left, list);
            list.add(root.vehicle);
            collectVehicles(root.right, list);
        }
    }

    void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Vehicle> list = (List<Vehicle>) ois.readObject();
            for (Vehicle v : list) insert(v);
        } catch (Exception e) {
            System.out.println("No previous BST data found or error reading file.");
        }
    }
}

class VehicleStack implements Serializable {
    transient Node top;

    void push(Vehicle v) {
        Node n = new Node(v);
        n.next = top;
        top = n;
    }

    Vehicle pop() {
        if (top == null) {
            System.out.println("Stack is empty");
            return null;
        }
        Vehicle v = top.vehicle;
        top = top.next;
        return v;
    }

    void displayStack() {
        Node temp = top;
        while (temp != null) {
            temp.vehicle.display();
            temp = temp.next;
        }
    }

    void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            List<Vehicle> list = new ArrayList<>();
            Node temp = top;
            while (temp != null) {
                list.add(temp.vehicle);
                temp = temp.next;
            }
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving stack: " + e.getMessage());
        }
    }

    void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Vehicle> list = (List<Vehicle>) ois.readObject();
            for (int i = list.size() - 1; i >= 0; i--) push(list.get(i));
        } catch (Exception e) {
            System.out.println("No previous stack data found or error reading file.");
        }
    }
}

public class VehicleParkingManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VehicleList list = new VehicleList();
        VehicleBST bst = new VehicleBST();
        VehicleStack stack = new VehicleStack();

        list.loadFromFile("vehicles.dat");
        bst.loadFromFile("bst.dat");
        stack.loadFromFile("stack.dat");

        while (true) {
            System.out.println("\n==== Vehicle Parking Management System ====");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Display All Vehicles");
            System.out.println("4. Sort Vehicles by Entry Time");
            System.out.println("5. Search Vehicle (Binary Search)");
            System.out.println("6. Insert/Search in BST");
            System.out.println("7. Stack: Last Parked Vehicles");
            System.out.println("8. Exit");
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
                    System.out.print("Enter Vehicle Number to Insert/Search in BST: ");
                    String searchBST = sc.nextLine();
                    Vehicle newVehicle = new Vehicle(searchBST, "Owner Name");
                    bst.insert(newVehicle);
                    System.out.print("Enter Vehicle Number to Search in BST: ");
                    String searchNumber = sc.nextLine();
                    bst.search(searchNumber);
                    break;
                case 7:
                    System.out.println("Stack of Recently Parked Vehicles:");
                    stack.displayStack();
                    break;
                case 8:
                    list.saveToFile("vehicles.dat");
                    bst.saveToFile("bst.dat");
                    stack.saveToFile("stack.dat");
                    System.out.println("Exiting and saving data...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
