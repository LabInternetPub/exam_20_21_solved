package cat.tecnocampus.rooms.application.dtos;

import java.util.ArrayList;
import java.util.List;

public class ClassroomDTO {
    private String name;
    private int capacity;
    private String orientation;
    private boolean plugs;
    private List<AllocationDTO> allocations = new ArrayList<>();

    public ClassroomDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isPlugs() {
        return plugs;
    }

    public void setPlugs(boolean plugs) {
        this.plugs = plugs;
    }

    public List<AllocationDTO> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<AllocationDTO> allocations) {
        this.allocations = allocations;
    }
}
