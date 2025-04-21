#!/bin/bash

LOG_FILE="/home/$USER/user_management.log"

# Check if script is run as root
if [[ $EUID -ne 0 ]]; then
  dialog --title "Permission Denied" --msgbox "This script must be run as root." 7 50
  clear
  exit 1
fi

# Ensure dialog is installed
if ! command -v dialog &> /dev/null; then
  echo "dialog command not found. Please install it first."
  exit 1
fi

log_action() {
  echo "[$(date)] $1" >> "$LOG_FILE"
}

add_user() {
  USERNAME=$(dialog --inputbox "Enter new username:" 8 40 3>&1 1>&2 2>&3 3>&-)
  PASSWORD=$(dialog --insecure --passwordbox "Enter password for $USERNAME:" 8 40 3>&1 1>&2 2>&3 3>&-)

  if id "$USERNAME" &>/dev/null; then
    dialog --msgbox "User '$USERNAME' already exists." 6 40
    return
  fi

  useradd -m "$USERNAME"
  echo "$USERNAME:$PASSWORD" | chpasswd
  dialog --msgbox "User '$USERNAME' added successfully." 6 40
  log_action "User added: $USERNAME"
}

delete_user() {
  USERNAME=$(dialog --inputbox "Enter username to delete:" 8 40 3>&1 1>&2 2>&3 3>&-)
  
  if ! id "$USERNAME" &>/dev/null; then
    dialog --msgbox "User '$USERNAME' does not exist." 6 40
    return
  fi

  dialog --yesno "Are you sure you want to delete user '$USERNAME'?" 7 50
  if [[ $? -eq 0 ]]; then
    userdel -r "$USERNAME"
    dialog --msgbox "User '$USERNAME' deleted." 6 40
    log_action "User deleted: $USERNAME"
  fi
}

modify_user() {
  USERNAME=$(dialog --inputbox "Enter username to modify:" 8 40 3>&1 1>&2 2>&3 3>&-)

  if ! id "$USERNAME" &>/dev/null; then
    dialog --msgbox "User '$USERNAME' does not exist." 6 40
    return
  fi

  OPTION=$(dialog --menu "Choose modification option:" 15 50 3 \
    1 "Change Username" \
    2 "Change Password" \
    3>&1 1>&2 2>&3 3>&-)

  case $OPTION in
    1)
      NEW_USERNAME=$(dialog --inputbox "Enter new username:" 8 40 3>&1 1>&2 2>&3 3>&-)
      usermod -l "$NEW_USERNAME" "$USERNAME"
      dialog --msgbox "Username changed to '$NEW_USERNAME'." 6 40
      log_action "Username changed from $USERNAME to $NEW_USERNAME"
      ;;
    2)
      NEW_PASSWORD=$(dialog --insecure --passwordbox "Enter new password:" 8 40 3>&1 1>&2 2>&3 3>&-)
      echo "$USERNAME:$NEW_PASSWORD" | chpasswd
      dialog --msgbox "Password updated for '$USERNAME'." 6 40
      log_action "Password updated for user $USERNAME"
      ;;
    *)
      dialog --msgbox "Invalid option." 6 30
      ;;
  esac
}

list_users() {
  cut -d: -f1 /etc/passwd | sort > /tmp/user_list.txt
  dialog --title "List of Users" --textbox /tmp/user_list.txt 20 50
  rm -f /tmp/user_list.txt
}

main_menu() {
  while true; do
    CHOICE=$(dialog --clear --title "User Management" \
      --menu "Choose an option:" 15 50 6 \
      1 "Add User" \
      2 "Delete User" \
      3 "Modify User" \
      4 "List Users" \
      5 "Exit" \
      3>&1 1>&2 2>&3 3>&-)

    case $CHOICE in
      1) add_user;;
      2) delete_user;;
      3) modify_user;;
      4) list_users;;
      5) clear; break;;
      *) dialog --msgbox "Invalid choice." 6 30;;
    esac
  done
}

main_menu
