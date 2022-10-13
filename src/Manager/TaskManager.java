package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generationId = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task task){
        task.setId(generationId++);
        tasks.put(task.getId(), task);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearAllTAsks() {
        tasks.clear();
    }

    public Task getTaskFromId(Integer id){

        return tasks.get(id);
    }

    public void removeTask(Task task) {
        if(tasks.get(task.getId()) != null)
            tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void addEpic(Epic epic) {
        epic.setId(generationId++);
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Epic> getEpics(){
        return new ArrayList<>(epics.values());
    }

    public void clearAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Epic getEpicFromId(int id){
        return epics.get(id);
    }

    public void deleteEpic(int id) {
        Epic deletedEpic = epics.remove(id);
        for (Integer subtaskId : deletedEpic.getSubTaskIds()) {
            subTasks.remove(subtaskId);
        }
    }

    public void removeEpic(Epic epic) {
        if (epics.get(epic.getId()) != null)
            epics.put(epic.getId(), epic);
    }

    public SubTask getEpicSubTaskList(int id) {
        Epic epic = epics.get(id); // не работает.
        return subTasks.get(epic);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subTasksId = epic.getSubTaskIds();
        if (subTasksId.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }
        String status = null;
        for (int subtasksId: subTasksId) {
            SubTask subTask = subTasks.get(subtasksId);
            if (status == null) {
                status = subTask.getStatus();
                continue;
            }
            if (status.equals(subTask.getStatus())) {
                continue;
            }
            epic.setStatus("IN_PROGRESS");
            return;
        }
        epic.setStatus(status);
    }

    public void addSubtask(SubTask subTask){
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTask.setId(generationId++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtaskId(subTask.getId());
        updateEpicStatus(epic);
    }

    public ArrayList<SubTask> getSubTask(){
        return new ArrayList<>(subTasks.values());
    }

    public void clearAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskId();
            updateEpicStatus(epic);
        }
    }

    public void deleteSubTask(Integer id) {
        SubTask deleteSubTask = subTasks.remove(id);
        if (deleteSubTask != null) {
            int deleteEpicId = deleteSubTask.getEpicId();
            Epic deleteEpic = epics.get(deleteEpicId);
            deleteEpic.removeSubTaskId(id);
            updateEpicStatus(deleteEpic);
        }
    }

    public SubTask getSubTaskId(int id){
        return subTasks.get(id);
    } // наботает

    public void removeSubTask() {
        // доделать.
    }



}

