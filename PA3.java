import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.Math;

public class makingTheSpartan1 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
//        We will flush out the output using this 
        BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
        
//        Create a hashmap to store the soldier objects 
        HashMap<String, soldier> map = new HashMap<>();
        
//        Create a heap with the capacity set to the input
        int numberOfSoldiers = scanner.nextInt();
        minHeap heap = new minHeap(numberOfSoldiers);
        
        
        for (int i = 0; i < numberOfSoldiers ; i++) {
            soldier sold = new soldier(scanner.next(), Long.parseLong(scanner.next()));
            map.put(sold.name, sold);
            heap.insert(sold);
        }
        
//        Prints the minheap
//        for (int i = 0; i <= heap.currentCap; i++) {
//            System.out.println(heap.masterArray[i]);
//        }
        
//        Now we get the number of queries 
        int numberOfQueiries = scanner.nextInt();
        
        for (int i = 0; i < numberOfQueiries; i++) {
            int queryType = scanner.nextInt();
            
//            Here we handle incrementing the score 
            if (queryType == 1) {
                String name = scanner.next();
                long toIncrement = scanner.nextLong();
                map.get(name).score += toIncrement;
                if (toIncrement > 0) {
                    heap.heapifyDown(map.get(name).position);
                }
                else {
                    heap.heapifyUp(map.get(name).position);
                }
                
            }
            
//            Here we handle deletion 
            else {
                long k = scanner.nextLong();
                heap.clearK(k, map);
                buffer.write(Integer.toString(heap.currentCap) + "\n");
            }
        }
        buffer.flush();
        scanner.close();
    }
}

class minHeap {
    static soldier[] masterArray;
    int currentCap;
    int maxCap;
    
    minHeap(int maxCap){
        this.maxCap = maxCap;
//      We initialize currentCap to 0 when there is nothing in the heap 
        this.currentCap = 0;
        this.masterArray = new soldier[this.maxCap + 5];
    }
    
    int getParent(int index) {
        if (index == 1) {
            return -1;
        }
        return (index / 2);
    }
    
    int getLeftChild(int index) {
        int position = index*2;
        if (position > currentCap) {
            return -1;
        }
        return (position);
    }
    
    int getRightChild(int index) {
        int position = index*2 + 1;
        if (position > currentCap) {
            return -1;
        }
        return (position);
    }
    
    boolean isLeaf(int index) {
        if (index < currentCap && index > currentCap / 2) {
            return true;
        }
        return false;
    }
    
    void swap(int index1, int index2) {
        masterArray[index1].position = index2;
        masterArray[index2].position = index1;
        soldier temp = masterArray[index1];
        masterArray[index1] = masterArray[index2];
        masterArray[index2] = temp;
    }
    
    void insert(soldier sold) {
        currentCap++;
        if (currentCap > maxCap) {
            System.out.println("Max Capacity exceeded");
            return;
        }
        masterArray[currentCap] = sold;
        sold.position = currentCap;
        heapifyUp(currentCap);
        
    }
    
    void heapifyUp(int pointer) {
        int parentPointer = getParent(pointer);
        while (parentPointer != -1) {
            if (masterArray[parentPointer].score > masterArray[pointer].score) {
                swap(parentPointer, pointer);
                pointer = parentPointer;
                parentPointer = getParent(pointer);
            }
            else {
                break;
            }
        }
    }
    
//    Call this starting at position 0 
//    void clearK(int k, int index) {
//        if (masterArray[index].score < k) {
//            deleteNode(index);
//            clearK(k, index);
//        }
//        else {
//            if (getLeftChild(index) != -1) {
//                clearK(k, getLeftChild(index));
//                if(getRightChild(index) != -1) {
//                    clearK(k, getRightChild(index));
//                }
//            }
//        }
//    }
    
    void clearK(long k, HashMap<String, soldier> map) {
        if (currentCap != 0 && masterArray[1].score < k) {
            map.remove(masterArray[1].name);
            deleteNode(1);
            
            if (currentCap != 0) {
                clearK(k, map);
            }
        }
    }
    
    void deleteNode(int index) {
//        First we swap the index with the last index
        swap(index, currentCap);
//        Next we destroy the last index 
        masterArray[currentCap] = null;
//        We decrement the capacity 
        currentCap--;
        if (index == 1) {
            heapifyDown(index);
        }
        else if (index != currentCap + 1) {
            if (masterArray[index].score < masterArray[getParent(index)].score) {
                heapifyUp(index);
            }
            else {
                heapifyDown(index);
    ;        }
        }
    }
    
    void heapifyDown(int pointer) {
        int leftIndex = getLeftChild(pointer);
        if (leftIndex == -1) {
            return;
        }
        long leftValue = masterArray[leftIndex].score;
        int rightIndex = getRightChild(pointer);
        long currentValue = masterArray[pointer].score;
        
        if (rightIndex != -1) {
            long rightValue = masterArray[rightIndex].score;
            if (leftValue < rightValue) {
                if (leftValue < currentValue) {
                    swap(pointer, leftIndex);
                    pointer = leftIndex;
                    heapifyDown(pointer);
                }
                else {
                    return;
                }
            }
            else {
                if (rightValue < currentValue) {
                    swap(pointer, rightIndex);
                    pointer = rightIndex;
                    heapifyDown(pointer);
                }
                else {
                    return;
                }
            }
        }
        else {
            if (leftValue < currentValue) {
                swap(pointer, leftIndex);
                pointer = leftIndex;
                heapifyDown(pointer);
            }
            else {
                return;
            }
        }
        
        
    }
    
    
    
}

class soldier {
    String name;
    long score;
    int position;
    
    soldier(String name, long score) {
        this.name = name;
        this.score = score;
    }
    
    public String toString() {
        return (name + " " + Long.toString(score) + " Position: " + 
    Integer.toString(position));
    }
    
}