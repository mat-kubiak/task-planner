/*
  Task Planner
  Copyright (C) Mateusz Kubiak

  Licensed under the GNU General Public License v3. 
  See LICENSE or visit <https://www.gnu.org/licenses/>.
*/

package com.github.matkubiak.taskplanner.model;

public class TaskUpdateDTO {

    private Long id;
    private String name;
    private String description;
    private boolean state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
