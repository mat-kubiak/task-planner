/*
  Task Planner
  Copyright (C) Mateusz Kubiak

  Licensed under the GNU General Public License v3. 
  See LICENSE or visit <https://www.gnu.org/licenses/>.
*/

package com.github.matkubiak.taskplanner.repository;

import com.github.matkubiak.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}