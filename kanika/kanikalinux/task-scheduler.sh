#!/bin/bash

TASK_DIR="/home/diksha/linux-project"
TASK_FILE="$TASK_DIR/tasks.txt"
LOG_FILE="$TASK_DIR/tasks.log"
mkdir -p "$TASK_DIR"
touch "$TASK_FILE" "$LOG_FILE"

log_action() {
    echo "$(date +'%Y-%m-%d %H:%M:%S') - $1" >> "$LOG_FILE"
}

add_task() {
    TASK=$(whiptail --inputbox "Enter the task description:" 10 60 3>&1 1>&2 2>&3) || return
    CATEGORY=$(whiptail --inputbox "Enter category (e.g., Work, Study, Personal):" 10 60 3>&1 1>&2 2>&3) || return
    START=$(whiptail --inputbox "Enter start time (HH:MM, 24hr):" 10 60 3>&1 1>&2 2>&3) || return
    END=$(whiptail --inputbox "Enter end time (HH:MM, 24hr):" 10 60 3>&1 1>&2 2>&3) || return

    if [[ ! "$START" =~ ^([01]?[0-9]|2[0-3]):[0-5][0-9]$ ]] || [[ ! "$END" =~ ^([01]?[0-9]|2[0-3]):[0-5][0-9]$ ]]; then
        whiptail --msgbox "Invalid time format!" 8 45
        return
    fi

    echo "$START - $END | $CATEGORY | $TASK" >> "$TASK_FILE"
    log_action "Added task: '$TASK' ($CATEGORY) from $START to $END"
    whiptail --msgbox "Task added successfully!" 8 45
}

list_tasks() {
    CURRENT_TIME=$(date +%H:%M)

    TEMP_FILE=$(mktemp)
    while IFS= read -r line; do
        START=$(echo "$line" | cut -d'-' -f1 | xargs)
        END=$(echo "$line" | cut -d'-' -f2 | cut -d'|' -f1 | xargs)
        [[ "$END" > "$CURRENT_TIME" ]] && echo "$line" >> "$TEMP_FILE"
    done < "$TASK_FILE"

    if [[ ! -s "$TEMP_FILE" ]]; then
        whiptail --msgbox "No upcoming tasks scheduled." 8 50
    else
        whiptail --textbox "$TEMP_FILE" 20 70
    fi
    rm -f "$TEMP_FILE"
}

search_task() {
    QUERY=$(whiptail --inputbox "Search tasks by keyword or category:" 10 60 3>&1 1>&2 2>&3) || return
    grep -i "$QUERY" "$TASK_FILE" > /tmp/search_result.txt

    if [[ ! -s /tmp/search_result.txt ]]; then
        whiptail --msgbox "No matching tasks found." 8 45
    else
        whiptail --textbox /tmp/search_result.txt 20 70
    fi
    rm -f /tmp/search_result.txt
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

    CHOICE=$(whiptail --title "Delete Task" --menu "Choose a task to delete:" 20 70 10 "${OPTIONS[@]}" 3>&1 1>&2 2>&3) || return

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

    CHOICE=$(whiptail --title "Edit Task" --menu "Select a task to edit:" 20 70 10 "${OPTIONS[@]}" 3>&1 1>&2 2>&3) || return

    OLD_TASK="${TASKS[CHOICE]}"
    NEW_TASK=$(whiptail --inputbox "Edit full line (format: HH:MM - HH:MM | Category | Task):" 10 70 "$OLD_TASK" 3>&1 1>&2 2>&3) || return

    TASKS[CHOICE]="$NEW_TASK"
    printf "%s\n" "${TASKS[@]}" > "$TASK_FILE"
    log_action "Edited task number $CHOICE"
    whiptail --msgbox "Task updated successfully." 8 45
}

main_menu() {
    while true; do
        OPTION=$(whiptail --title "Bash Task Scheduler" --menu "Choose an option:" 20 60 10 \
            "1" "Add New Task" \
            "2" "List Upcoming Tasks" \
            "3" "Search Task" \
            "4" "Edit Task" \
            "5" "Delete Task" \
            "6" "Exit" 3>&1 1>&2 2>&3)

        case $OPTION in
            1) add_task ;;
            2) list_tasks ;;
            3) search_task ;;
            4) edit_task ;;
            5) delete_task ;;
            6) whiptail --msgbox "Goodbye!" 8 45; break ;;
            *) whiptail --msgbox "Invalid option." 8 45 ;;
        esac
    done
}

# Root permission check (optional based on your environment)
if [ "$EUID" -ne 0 ]; then
    whiptail --msgbox "Please run this script as root!" 8 45
    exit 1
fi

main_menu
