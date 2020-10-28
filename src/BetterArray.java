/**
 * BetterArray class implement a smart array that will automatically 
 * increase/decrease the capacity when needed.
 * CS 310-002.
 * @author Bao Vo
 * @param <T> generic type
 */ 
public class BetterArray<T> {
    
    /**
     * default initial capacity / minimum capacity.
     */ 
    private static final int DEFAULT_CAPACITY = 2;
    
    /**
     * Underlying array.
     */
    private T[] data; 
    
    /**
     *  The capacity of the current array.
     */ 
    private int capacity;
    
    /**
     * The current size of the current array.
     */ 
    private int dataSize;
    
    /**
     * Contructor of the array which initialize all its field to default
     * with the capacity of default and size of 0.
     */ 
    @SuppressWarnings("unchecked")
    public BetterArray() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        dataSize = 0;
    }
    
    /**
     * Contructor of the array with an assigned capacity.
     * @param initialCapacity the number of capacity
     * @throw IllegalArgumentException if initialCapacity is smaller than 1
     */ 
    @SuppressWarnings("unchecked")
    public BetterArray(int initialCapacity) {  
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than 1");
        }
        else {
            this.data = (T[]) new Object[initialCapacity];
            this.capacity = initialCapacity;
            this.dataSize = 0;
        }
    }
    
    /**
     * report number of elements in the smart array.
     * @return the number of elements in the smart array
     */ 
    public int size() { 
        return this.dataSize;
    }  
    
    /**
     * report max number of elements before the next expansion.
     * @return the current capacity
     */ 
    public int capacity() { 
        return this.capacity;
    }
    
    /**
     * Add an element to the end of the array, double the capacity if no space is available.
     * @param value the element 
     * @return true for an addition.
     */ 
    @SuppressWarnings("unchecked")
    public boolean append(T value) {
        if (dataSize == capacity) {
            this.capacity *= 2;
            BetterArray<T> tempData = clone();
            this.data = tempData.data;
        }
        this.data[dataSize] = value;
        this.dataSize++;
        return true;
    }
    
    /**
     * Add an element to a specified index.
     * Shift elements and double the capacity if needed.
     * @param index the index
     * @param value the element
     * @throw IndexOutOfBoundsException for invalid index
     */ 
    @SuppressWarnings("unchecked")
    public void add(int index, T value) {
        if (dataSize == capacity) {
            this.capacity *= 2;
            BetterArray<T> originalData = clone();
            this.data = originalData.data;
        }
        if (index < 0 || index > capacity) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        for (int i = dataSize; i > index; i--) {
            this.data[i] = this.data[i-1];
        }
        this.data[index] = value;
        this.dataSize++;
    }
    
    /**
     * Get an item at a specified index.
     * @param index the index
     * @throw IndexOutOfBoundsException for invalid index
     * @return the element at the given index
     */ 
    public T get(int index){
        if (index < 0 || index > capacity) {
            throw new IndexOutOfBoundsException("Invalid index");
        }    
        return this.data[index];
    }
    
    /**
     * Replace the item at the given index to be a new value.
     * @param index the index
     * @param value the new value
     * @throw IndexOutOfBoundsException for invalid index
     * @return old item at index
     */ 
    public T replace(int index, T value){
        if (index < 0 || index > dataSize) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        T oldData = data[index];
        this.data[index] = value;
        return oldData;
    }
    
    /**
     * Delete an element at the given index
     * Specify the capacity by decreasing it by half if the number of elements falls below 1/4 of the capacity 
     * but greater than the default capacity.
     * @param index the index
     * @throw IndexOutOfBoundsException for invalid index
     * @return the element at position index
     */ 
    @SuppressWarnings("unchecked")
    public T delete(int index){
        if (index < 0 || index >= dataSize) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        T removedData = data[index];
        for (int i = index; i < (dataSize-1); i++) {
            this.data[i] = this.data[i+1];
        }
        this.dataSize--;
        if (((this.dataSize * 4) < this.capacity) && ((this.capacity / 2) >= DEFAULT_CAPACITY)) {
            this.capacity /= 2;
        }
        return removedData;
    }  
    
    /**
     * Find the index of the first occurence.
     * @param value the value
     * @return the index of the first occurence or -1 if not found
     */ 
    public int firstIndexOf(T value){
        int index = -1;
        for (int i = 0; i < dataSize; i++) {
            if (data[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Change the max number of items allowed before next expansion to newCapacity 
     * if the new capacity is greater than the default capacity
     * and large enough to accommodate the current number of items in the array.
     * @param newCapacity the new capacity
     * @return true if the new capacity get applied, false otherwise.
     * 
     */ 
    @SuppressWarnings("unchecked")
    public boolean ensureCapacity(int newCapacity){
        if ((newCapacity > DEFAULT_CAPACITY) && (newCapacity > dataSize)) {
            this.capacity = newCapacity;
            return true;
        }
        return false;
    }
    
    /**
     * Make a copy of all current values with the same capacity.
     * @return the new array that contains all the copied values.
     */ 
    public BetterArray<T> clone() {
        BetterArray<T> newData = new BetterArray<T>(this.capacity);
        for (int i = 0; i < dataSize; i++) {
            newData.add(i, this.data[i]);
        }
        return newData;
    }
    
    /**
     * The main function which is used to test all the methods.
     * @param args the command line arguments as an array of strings
     */ 
    public static void main(String args[]) {
        //create a smart array of integers
        BetterArray<Integer> nums = new BetterArray<>();
        if ((nums.size() == 0) && (nums.capacity() == 2)){
            System.out.println("Yay 1");
        }
        //append some numbers 
        for (int i=0; i<3;i++)
            nums.add(i,i*2);
        
        if (nums.size()==3 && nums.get(2) == 4 && nums.capacity() == 4 ){
            System.out.println("Yay 2");
        }
        
        //create a smart array of strings
        BetterArray<String> msg = new BetterArray<>();
        
        //insert some strings
        msg.add(0,"world");
        msg.add(0,"hello");
        msg.add(1,"new");
        msg.append("!");
        
        //replace and checking
        if (msg.get(0).equals("hello") && msg.replace(1,"beautiful").equals("new") 
                && msg.size() == 4 && msg.capacity() == 4 ){
            System.out.println("Yay 3");
        }
        
        //change capacity
        if (!msg.ensureCapacity(0) && !msg.ensureCapacity(3) && msg.ensureCapacity(20)
                && msg.capacity() == 20){
            System.out.println("Yay 4");
        }  
        
        //delete and shrinking
        if (msg.delete(1).equals("beautiful") && msg.get(1).equals("world")  
                && msg.size() == 3 && msg.capacity() == 10 ){
            System.out.println("Yay 5");
        }
        
        //firstIndexOf and clone
        //remember what == does on objects... not the same as .equals()
        BetterArray<String> msgClone = msg.clone();
        if (msgClone != msg && msgClone.get(1) == msg.get(1)
                && msgClone.size() == msg.size()
                && msgClone.capacity() == msg.capacity()
                && msgClone.firstIndexOf("world") == 1
                && msgClone.firstIndexOf("beautiful") == -1) {
            System.out.println("Yay 6");
        }
    }
    
    /**
     * Print a string representation of the object.
     * @return the string representation
     */ 
    public String toString() {
        if(size() == 0) return "";
        
        StringBuffer sb = new StringBuffer();
        sb.append(get(0));
        for(int i = 1; i < size(); i++) {
            sb.append(", ");
            sb.append(get(i));
        }
        return sb.toString();
    }
}