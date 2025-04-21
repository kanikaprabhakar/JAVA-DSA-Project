#!/bin/bash

TASK_FILE="$HOME/task_scheduler/tasks.txt"
LOG_FILE="$HOME/task_scheduler/task_scheduler.log"
mkdir -p "$HOME/task_scheduler"
touch "$TASK_FILE" "$LOG_FILE"

log_action() {
    echo "$(date +'%Y-%m-%d %H:%M:%S') - $1" >> "$LOG_FILE"
}

add_task() {
    TASK=$(whiptail --inputbox "Enter the task description:" 10 60 3>&1 1>&2 2>&3)
    [ -z "$TASK" ] && return

    TIME=$(whiptail --inputbox "Enter time to execute (HH:MM, 24hr format):" 10 60 3>&1 1>&2 2>&3)
    if [[ ! "$TIME" =~ ^([01]?[0-9]|2[0-3]):[0-5][0-9]$ ]]; then
        whiptail --msgbox "Invalid time format!" 8 45
        return
    fi

    echo "$TIME - $TASK" >> "$TASK_FILE"
    log_action "Added task: '$TASK' at $TIME"
    whiptail --msgbox "Task added successfully!" 8 45
}

list_tasks() {
    if [[ ! -s "$TASK_FILE" ]]; then
        whiptail --msgbox "No tasks scheduled." 8 45
    else
        whiptail --textbox "$TASK_FILE" 20 70
    fi
}

delete_task() {
    if [[ ! -s "$TASK_FILE" ]]; then
        whiptail --msgbox "No tasks to delete." 8 45
        return
    fi

    mapfile -t TASKS < "$TASK_FILE"
    OPTIONS=()
    for i in "${!TASKS[@]}"; do
        OPTIONS+=("$i" "${TASKS[$i]}")
    done

    CHOICE=$(whiptail --title "Delete Task" --menu "Choose a task to delete:" 20 70 10 "${OPTIONS[@]}" 3>&1 1>&2 2>&3)
    [ -z "$CHOICE" ] && return

    unset 'TASKS[CHOICE]'
    printf "%s\n" "${TASKS[@]}" > "$TASK_FILE"
    log_action "Deleted task number $CHOICE"
    whiptail --msgbox "Task deleted successfully." 8 45
}

edit_task() {
    if [[ ! -s "$TASK_FILE" ]]; then
        whiptail --msgbox "No tasks to edit." 8 45
        return
    fi

    mapfile -t TASKS < "$TASK_FILE"
    OPTIONS=()
    for i in "${!TASKS[@]}"; do
        OPTIONS+=("$i" "${TASKS[$i]}")
    done

    CHOICE=$(whiptail --title "Edit Task" --menu "Select a task to edit:" 20 70 10 "${OPTIONS[@]}" 3>&1 1>&2 2>&3)
    [ -z "$CHOICE" ] && return

    OLD_TASK="${TASKS[CHOICE]}"
    NEW_TASK=$(whiptail --inputbox "Edit task (format: HH:MM - Task)" 10 70 "$OLD_TASK" 3>&1 1>&2 2>&3)
    [ -z "$NEW_TASK" ] && return

    TASKS[CHOICE]="$NEW_TASK"
    printf "%s\n" "${TASKS[@]}" > "$TASK_FILE"
    log_action "Edited task number $CHOICE"
    whiptail --msgbox "Task updated successfully." 8 45
}

main_menu() {
    while true; do
        OPTION=$(whiptail --title "Bash Task Scheduler" --menu "Choose an option:" 20 60 10 \
            "1" "Add New Task" \
            "2" "List Tasks" \
            "3" "Edit Task" \
            "4" "Delete Task" \
            "5" "Exit" 3>&1 1>&2 2>&3)

        case $OPTION in
            1) add_task ;;
            2) list_tasks ;;
            3) edit_task ;;
            4) delete_task ;;
            5) whiptail --msgbox "Goodbye!" 8 45; break ;;
            *) whiptail --msgbox "Invalid option." 8 45 ;;
        esac
    done
}

# Check root permission
if [ "$EUID" -ne 0 ]; then
    whiptail --msgbox "Please run this script as root!" 8 45
    exit 1
fi

main_menu
