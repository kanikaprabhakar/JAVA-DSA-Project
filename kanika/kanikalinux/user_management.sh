#!/bin/bash

LOG_FILE="/home/$USER/user_management.log"

# Ensure script is run as root
if [[ $EUID -ne 0 ]]; then
    whiptail --title "Permission Denied" --msgbox "This script must be run as root." 10 50
    exit 1
fi

# Logging function
log_action() {
    echo "$(date): $1" >> "$LOG_FILE"
}

# Add user function
add_user() {
    username=$(whiptail --inputbox "Enter the new username:" 10 60 --title "Add User" 3>&1 1>&2 2>&3)
    exitstatus=$?
    if [ $exitstatus = 0 ]; then
        if id "$username" &>/dev/null; then
            whiptail --msgbox "User already exists!" 10 40
        else
            password=$(whiptail --passwordbox "Enter password for $username:" 10 60 --title "Set Password" 3>&1 1>&2 2>&3)
            useradd -m "$username"
            echo "$username:$password" | chpasswd
            whiptail --msgbox "User $username added successfully!" 10 50
            log_action "Added user $username"
        fi
    fi
}

# Delete user function
delete_user() {
    username=$(whiptail --inputbox "Enter username to delete:" 10 60 --title "Delete User" 3>&1 1>&2 2>&3)
    if id "$username" &>/dev/null; then
        if whiptail --yesno "Are you sure you want to delete $username?" 10 60; then
            userdel -r "$username"
            whiptail --msgbox "User $username deleted!" 10 50
            log_action "Deleted user $username"
        fi
    else
        whiptail --msgbox "User does not exist!" 10 40
    fi
}

# Modify user
modify_user() {
    username=$(whiptail --inputbox "Enter username to modify:" 10 60 --title "Modify User" 3>&1 1>&2 2>&3)
    if id "$username" &>/dev/null; then
        newname=$(whiptail --inputbox "Enter new username for $username:" 10 60 --title "Rename User" 3>&1 1>&2 2>&3)
        usermod -l "$newname" "$username"
        whiptail --msgbox "$username renamed to $newname" 10 50
        log_action "Renamed user $username to $newname"
    else
        whiptail --msgbox "User does not exist!" 10 40
    fi
}

# List users
list_users() {
    cut -d: -f1 /etc/passwd > /tmp/userlist.txt
    whiptail --textbox /tmp/userlist.txt 20 60 --title "List of Users"
    rm /tmp/userlist.txt
    log_action "Listed users"
}

# Main Menu
while true; do
    CHOICE=$(whiptail --title "Linux User Management" --menu "Choose an action:" 20 60 10 \
        1 "Add User" \
        2 "Delete User" \
        3 "Modify User" \
        4 "List Users" \
        5 "Exit" 3>&1 1>&2 2>&3)

    case $CHOICE in
        1) add_user ;;
        2) delete_user ;;
        3) modify_user ;;
        4) list_users ;;
        5) exit ;;
        *) whiptail --msgbox "Invalid Option" 10 40 ;;
    esac
done
