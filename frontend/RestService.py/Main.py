import tkinter as tk
import pika
from datetime import datetime
import json
import tkinter.messagebox
import requests
import urllib.parse
import threading
import tkinter.messagebox
from tkcalendar import DateEntry
from tkinter import ttk, messagebox
from tkinter import simpledialog


def validate_trip_submission(city, country, selected_date):
    if not all([city, country, selected_date]):
        return "All fields must be filled", False
    try:
        # Attempt to parse the datetime to check format
        datetime.strptime(selected_date, "%Y-%m-%d")
    except ValueError:
        return "Invalid date format (expected YYYY-MM-DD)", False
    return "Valid", True


def validate_login(username, password):
    if not username or not password:
        return "Username and password cannot be empty", False
    return "Valid", True


def validate_registration(username, password):
    if not username or not password:
        return "Username and password cannot be empty", False
    if len(password) < 6:
        return "Password must be at least 6 characters long", False
    return "Valid", True


def save_credentials(username, password, register_label):
    try:
        url = "http://51.105.37.8:8080/myrestservice-1.0/user/register"  # The endpoint url should be placed here
        data = {'username': username, 'password': password}
        response = requests.post(url, json=data)

        if response.status_code == 200:
            register_label.config(text="Registration Successful!", fg='green')
        else:
            register_label.config(text=f"Registration Failed: {response.text}", fg='red')
            print(response.text)
    except Exception as e:
        register_label.config(text=f"Registration Failed: {e}", fg='red')


def callback(ch, method, properties, body):
    message = body.decode('utf-8')
    print(f" [x] Received {message}")
    # Process the message here
    # Note: Since this is a separate thread, be careful with updating GUI elements directly


def start_consumer():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='task_queue', durable=True)
    channel.basic_consume(queue='task_queue', on_message_callback=callback, auto_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()


def fetch_weather(user_id, trip_id):
    # The endpoint Url to get trip details should be placed here
    url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/trips/details?user_id={user_id}&trip_id={trip_id}"

    try:
        response = requests.get(url)
        if response.status_code == 200:
            trip_data = response.json()
            location = trip_data.get('location')
            date = trip_data.get('datetime')

            if location and date:
                api_key = "M82Q85B6B99D4WY8BZKKMGCV3"
                encoded_location = urllib.parse.quote(location)
                weather_url = f"https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/{encoded_location}?key={api_key}&start={date}&end={date}"

                weather_response = requests.get(weather_url)
                weather_response.raise_for_status()
                weather_data = weather_response.json()
                display_weather_data(weather_data)
            else:
                tkinter.messagebox.showinfo("Info", "Location or date not found for the given trip.")
        else:
            tkinter.messagebox.showinfo("Info", "No trip details found for the given ID.")
    except requests.RequestException as e:
        print(f"Error: {e}")
        tkinter.messagebox.showerror("Error", "Failed to fetch trip details.")


def open_login_window():
    global navigation_stack
    root.title("Travel Buddy Finder")

    main_menu_frame = tk.Frame(root, bg=BG_COLOR)
    main_menu_frame.pack(fill='both', expand=True)

    register_button = tk.Button(main_menu_frame, text="Register", command=lambda: show_frame(open_register_window),
                                bg=BTN_COLOR, font=BTN_FONT)
    register_button.pack(padx=10, pady=10)

    login_button = tk.Button(main_menu_frame, text="Login", command=lambda: show_frame(on_login), bg=BTN_COLOR,
                             font=BTN_FONT)
    login_button.pack(padx=10, pady=10)

    if not navigation_stack:
        navigation_stack.append(open_login_window)


def on_login():
    for widget in root.winfo_children():
        widget.pack_forget()
    root.title("Login")

    login_frame = tk.Frame(root, bg=BG_COLOR)
    login_frame.pack(fill='both', expand=True)

    tk.Label(login_frame, text="Username:", font=LABEL_FONT, bg=BG_COLOR).pack(padx=50, pady=5)
    username_entry = tk.Entry(login_frame, font=ENTRY_FONT)
    username_entry.pack(pady=5)

    tk.Label(login_frame, text="Password:", font=LABEL_FONT, bg=BG_COLOR).pack(padx=50, pady=5)
    password_entry = tk.Entry(login_frame, show="*", font=ENTRY_FONT)
    password_entry.pack(pady=5)

    login_label = tk.Label(login_frame, text="")
    login_label.pack()

    def attempt_login():
        username = username_entry.get()
        password = password_entry.get()
        message, is_valid = validate_login(username, password)
        if not is_valid:
            login_label.config(text=message, fg='red')
            return

        # Send login request to backend
        url = "http://51.105.37.8:8080/myrestservice-1.0/user/login"
        data = {'username': username, 'password': password}
        try:
            response = requests.post(url, json=data)
            if response.status_code == 200:
                login_label.config(text="Login Successful!", fg='green')
                root.after(1000, lambda: show_frame(open_trip_suggestion_window))
            else:
                login_label.config(text="Login Failed. Try again.", fg='red')
        except requests.RequestException as e:
            login_label.config(text=f"Login Failed: {e}", fg='red')

    login_button = tk.Button(login_frame, text="Login", command=attempt_login, bg=BTN_COLOR, font=BTN_FONT)
    login_button.pack(padx=10, pady=10)
    back_button = tk.Button(login_frame, text="Back to Main Menu", command=logout, bg=BTN_COLOR, font=BTN_FONT)
    back_button.pack(padx=10, pady=10)


def open_register_window():
    for widget in root.winfo_children():
        widget.pack_forget()
    root.title("Registration")

    register_frame = tk.Frame(root, bg=BG_COLOR)
    register_frame.pack(fill='both', expand=True)

    tk.Label(register_frame, text="Username:", font=LABEL_FONT, bg=BG_COLOR).pack(padx=10, pady=5)
    username_entry = tk.Entry(register_frame, font=ENTRY_FONT)
    username_entry.pack(padx=10, pady=5)

    tk.Label(register_frame, text="Password:", font=LABEL_FONT, bg=BG_COLOR).pack(padx=10, pady=5)
    password_entry = tk.Entry(register_frame, show="*", font=ENTRY_FONT)
    password_entry.pack(padx=10, pady=5)

    register_label = tk.Label(register_frame, text="")
    register_label.pack()

    register_button = tk.Button(register_frame, text="Register",
                                command=lambda: save_credentials(username_entry.get(), password_entry.get(), register_label, ))
    register_button.config(bg=BTN_COLOR, font=BTN_FONT)
    register_button.pack(padx=10, pady=10)

    back_button = tk.Button(register_frame, text="Back to Main Menu", command=logout, bg=BTN_COLOR, font=BTN_FONT)
    back_button.pack(padx=10, pady=10)


def display_trips(user_id):
    # Replace with your actual endpoint for fetching trips
    url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/trips/byUser?userId={user_id}"

    try:
        response = requests.get(url)
        if response.status_code == 200:
            trips = response.json()

            # Display the trips in a new window or a dialog
            trip_details_window = tk.Toplevel(root)
            trip_details_window.title("Trip Details")
            trip_details_window.geometry("400x300")
            text = tk.Text(trip_details_window)
            text.pack()

            for trip in trips:
                user_id, trip_id, location, datetime = trip['user_id'], trip['trip_id'], trip['location'], trip[
                    'datetime']
                text.insert(tk.END, f"User ID: {user_id}, Trip ID: {trip_id}, Location: {location}, Date: {datetime}\n")

        else:
            tkinter.messagebox.showerror("Error", f"Failed to fetch trips: {response.text}")

    except requests.RequestException as e:
        tkinter.messagebox.showerror("Error", f"An error occurred: {e}")


def display_data():
    trip_id = simpledialog.askstring("Trip ID", "Enter your Trip ID:")
    if trip_id:
        url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/trips/details?tripId={trip_id}"
        response = requests.get(url)
        if response.status_code == 200:
            trip_data = response.json()
            trip_details_str = f"User ID: {trip_data['user_id']}\nTrip ID: {trip_data['trip_id']}\nLocation: {trip_data['location']}\nDatetime: {trip_data['datetime']}"
            messagebox.showinfo("Trip Details", trip_details_str)
            show_weather_details(trip_data['weather'])
            show_hotel_details(trip_data['hotels'])
        else:
            messagebox.showinfo("Info", "No trip details found for the given User ID.")


def open_trip_suggestion_window():
    global navigation_stack
    # Clearing the stack to ensure "Back" or "Previous" can't go beyond this screen
    navigation_stack = [open_trip_suggestion_window]

    for widget in root.winfo_children():
        widget.pack_forget()
    root.title("Main Menu")

    trip_frame = tk.Frame(root, bg=BG_COLOR)
    trip_frame.pack(fill='both', expand=True)

    # Section for suggesting a trip
    suggest_trip_button = tk.Button(trip_frame, text="Request New Trip", command=suggest_trip, bg=BTN_COLOR,
                                    font=BTN_FONT)
    suggest_trip_button.pack(pady=20)

    view_details_button = tk.Button(trip_frame, text="View My Trip Details", command=display_data, bg=BTN_COLOR, font=BTN_FONT)
    view_details_button.pack(pady=10)

    query_button = tk.Button(trip_frame, text="Query Trips", command=open_query_trips, bg=BTN_COLOR, font=BTN_FONT)
    query_button.pack(padx=10, pady=10)

    # Navigation buttons
    logout_button = tk.Button(trip_frame, text="Logout", command=logout, bg=BTN_COLOR, font=BTN_FONT)
    logout_button.pack(padx=10, pady=10)


def open_query_trips():
    navigation_stack.append(open_query_trips)
    for widget in root.winfo_children():
        widget.pack_forget()
    root.title("Query Trips")

    query_frame = tk.Frame(root, bg=BG_COLOR)
    query_frame.pack(fill='both', expand=True)

    # Section for querying existing trips
    tk.Label(query_frame, text="Enter User ID:", font=LABEL_FONT, bg=BG_COLOR).pack(padx=10, pady=5)
    user_id_query_entry = tk.Entry(query_frame, font=ENTRY_FONT)
    user_id_query_entry.pack(padx=10, pady=5)

    query_result_frame = tk.Frame(query_frame)
    query_result_frame.pack(padx=10, pady=10)

    def express_interest(user_id, trip_id):
        # Replace with your actual endpoint for expressing interest
        url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/trips/expressInterest?userId={user_id}&tripId={trip_id}"
        try:
            response = requests.post(url)

            if response.status_code == 200:
                tk.messagebox.showinfo("Interest Expressed", f"You have expressed interest in trip ID: {trip_id}")
            else:
                tk.messagebox.showerror("Error", f"Failed to express interest: {response.text}")

        except Exception as e:
            tk.messagebox.showerror("Error", f"An error occurred: {e}")

    def query_trips():
        user_id = user_id_query_entry.get()
        # Replace with your actual endpoint for fetching trips
        url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/trips/byUser?userId={user_id}"

        try:
            response = requests.get(url)
            if response.status_code == 200:
                trips = response.json()
                # Clear previous results
                for widget in query_result_frame.winfo_children():
                    widget.destroy()
                for trip in trips:
                    trip_label = tk.Label(query_result_frame, text=f"Trip ID: {trip['trip_id']}, Location: {trip['location']}, Date: {trip['datetime']}")
                    trip_label.pack(side=tk.LEFT)
                    interest_button = tk.Button(query_result_frame, text="Express Interest",
                                                command=lambda trip_id=trip['trip_id']: express_interest(user_id, trip_id))
                    interest_button.pack(side=tk.RIGHT)
            else:
                tk.messagebox.showerror("Error", f"Failed to fetch trips: {response.text}")

        except requests.RequestException as e:
            tk.messagebox.showerror("Error", f"An error occurred: {e}")
    query_button = tk.Button(query_frame, text="Query Trips", command=query_trips, bg=BTN_COLOR, font=BTN_FONT)
    query_button.pack(padx=10, pady=10)

    # Navigation buttons
    prev_button = tk.Button(query_frame, text="Previous", command=lambda: go_back(query_frame), bg=BTN_COLOR, font=BTN_FONT)
    prev_button.pack(padx=10, pady=10)


def get_ids_json(city, country, datetime_str):
    try:
        url = "http://51.105.37.8:8080/myrestservice-1.0/myrest/tripDetails"
        params = {'city': city, 'country': country, 'datetime': datetime_str}
        response = requests.get(url, params=params)
        response.raise_for_status()
        return response.json
    except requests.RequestException as e:
        print(f"Request error: {e}")
        return None
    except json.JSONDecodeError:
        print("Error decoding JSON response")
        return None


def suggest_trip():
    for widget in root.winfo_children():
        if isinstance(widget, tk.Toplevel):
            widget.destroy()
        else:
            widget.pack_forget()
    root.title("Trip Details")

    details_frame = tk.Frame(root, bg=BG_COLOR)
    details_frame.pack(fill='both', expand=True)

    # City Entry
    tk.Label(details_frame, text="City:").pack()
    city_entry = tk.Entry(details_frame)
    city_entry.pack()

    # Country Entry
    tk.Label(details_frame, text="Country:").pack()
    country_entry = tk.Entry(details_frame)
    country_entry.pack()

    # Date and Time Entry
    tk.Label(details_frame, text="Date and Time:").pack()
    date_entry = DateEntry(details_frame, date_pattern='y-mm-dd')
    date_entry.pack()

    # Time Entry (hours and minutes)
    tk.Label(details_frame, text="Hour:").pack()
    hour_entry = tk.Spinbox(details_frame, from_=0, to=23, width=5, format="%02.0f")
    hour_entry.pack()

    tk.Label(details_frame, text="Minute:").pack()
    minute_entry = tk.Spinbox(details_frame, from_=0, to=59, width=5, format="%02.0f")
    minute_entry.pack()

    def on_submit():
        city = city_entry.get()
        country = country_entry.get()
        date = date_entry.get_date().strftime('%Y-%m-%d')
        hour = int(hour_entry.get())
        minute = minute_entry.get()

        # Convert 24-hour format to 12-hour format with AM/PM
        am_pm = "AM" if hour < 12 else "PM"
        hour = hour if hour <= 12 else hour - 12
        datetime_str = f"{date} {hour}:{minute} {am_pm}"
        ids_json = get_ids_json(city, country, datetime_str)
        # Use ids_json directly if it's already a dictionary
        if isinstance(ids_json, dict):
            ids_data = ids_json
        else:
            # Only parse if ids_json is a str, bytes, or bytearray
            try:
                ids_data = json.loads(ids_json)
            except json.JSONDecodeError:
                print("Error decoding JSON response")
                ids_data = {}

        submit_trip(city, country, datetime_str, details_frame)

        # Submit button
    submit_button = tk.Button(details_frame, text="Submit Trip", command=on_submit)
    submit_button.config(bg=BTN_COLOR, font=BTN_FONT)
    submit_button.pack(padx=10, pady=10)

    prev_button = tk.Button(details_frame, text="Previous", command=lambda: go_back(details_frame), bg=BTN_COLOR, font=BTN_FONT)
    prev_button.pack(padx=10, pady=10)

    navigation_stack.append(suggest_trip)


def submit_trip(city, country, selected_date, main_frame):
    message, is_valid = validate_trip_submission(city, country, selected_date)
    if not is_valid:
        tk.Label(main_frame, text=message, fg='red').pack()
        return

    try:
        # Format the selected date to match the expected format
        formatted_date_str = datetime.strptime(selected_date, "%d.%m.%Y %I:%M%p").strftime("%d.%m.%Y %H:%M")
        url = f"http://51.105.37.8:8080/myrestservice-1.0/myrest/tripDetails"
        params = {'city': city, 'country': country, 'datetime': formatted_date_str}
        response = requests.get(url, params=params)

        if response.status_code != 200:
            raise Exception(f"Server responded with status code: {response.status_code}")

        trip_data = response.json()

        # Clear the main_frame and display trip details
        for widget in main_frame.winfo_children():
            widget.destroy()

        # Extract and display weather and hotel data
        weather_data = trip_data.get('weather', {})
        hotel_data = trip_data.get('hotels', {})
        display_weather_and_hotel_buttons(main_frame, weather_data, hotel_data)

    except Exception as e:
        messagebox.showerror("Error", f"Failed to fetch trip details: {e}")


def display_weather_and_hotel_buttons(main_frame, weather_data, hotel_data):
    weather_btn = tk.Button(main_frame, text="Weather Forecast",
                            command=lambda: show_weather_details(weather_data))
    weather_btn.pack(pady=10)

    hotels_btn = tk.Button(main_frame, text="Hotels Available",
                           command=lambda: show_hotel_details(hotel_data))
    hotels_btn.pack(pady=10)

    prev_button = tk.Button(main_frame, text="Previous", command=lambda: go_back(main_frame), bg=BTN_COLOR, font=BTN_FONT)
    prev_button.pack(padx=10, pady=10)


def display_weather_data(weather_data):
    weather_window = tk.Toplevel(root)
    weather_window.title("Weather Forecast")
    weather_window.geometry("600x500")

    text = tk.Text(weather_window)
    text.pack()
    text.insert(tk.END, json.dumps(weather_data, indent=4))


def show_weather_details(weather_data):
    # Create a new window for the weather details
    weather_window = tk.Toplevel(root)
    weather_window.title("Weather Forecast")
    weather_window.geometry("600x500")

    # Create a Treeview widget
    tree = ttk.Treeview(weather_window)

    # Define our columns
    tree['columns'] = ('key', 'value')

    # Format our columns
    tree.column("#0", width=0, stretch=tk.NO)
    tree.column("key", anchor=tk.W, width=120)
    tree.column("value", anchor=tk.W, width=180)

    # Create headings
    tree.heading("#0", text="", anchor=tk.W)
    tree.heading("key", text="Key", anchor=tk.W)
    tree.heading("value", text="Value", anchor=tk.W)

    # Add data to the treeview
    for i, (key, value) in enumerate(weather_data.items()):
        tree.insert(parent='', index='end', iid=i, text='', values=(key.capitalize(), value))

    tree.pack(fill='both', expand=True)


def show_hotel_details(hotel_data):
    hotel_window = tk.Toplevel(root)
    hotel_window.title("Hotels Available")
    hotel_window.geometry("600x500")

    # Create a Treeview widget
    tree = ttk.Treeview(hotel_window)

    # Define our columns
    tree["columns"] = ("fullName", "lon", "lat", "label", "id", "_score")

    # Format our columns
    tree.column("#0", width=0, stretch=tk.NO)  # Hide the default column
    tree.column("fullName", anchor=tk.W, width=200)
    tree.column("lon", anchor=tk.W, width=100)
    tree.column("lat", anchor=tk.W, width=100)
    tree.column("label", anchor=tk.W, width=150)
    tree.column("id", anchor=tk.W, width=80)
    tree.column("_score", anchor=tk.W, width=80)

    # Create Headings
    tree.heading("#0", text="", anchor=tk.W)
    tree.heading("fullName", text="Hotel Name", anchor=tk.W)
    tree.heading("lon", text="Longitude", anchor=tk.W)
    tree.heading("lat", text="Latitude", anchor=tk.W)
    tree.heading("label", text="Label", anchor=tk.W)
    tree.heading("id", text="ID", anchor=tk.W)
    tree.heading("_score", text="Score", anchor=tk.W)

    # Inserting each hotel into the tree
    hotels = hotel_data.get("results", {}).get("hotels", [])
    for hotel in hotels:
        tree.insert("", "end", values=(
            hotel.get("fullName"),
            hotel.get("location", {}).get("lon"),
            hotel.get("location", {}).get("lat"),
            hotel.get("label"),
            hotel.get("id"),
            hotel.get("_score")
        ))

    # Pack the Treeview widget
    tree.pack(fill="both", expand=True)

    # If you want to add a label at the top with the location name, you would do something like this:
    if hotels:
        location_label = tk.Label(hotel_window, text=f"Location: {hotels[0].get('locationName')}")
        location_label.pack(side="top", fill="x")


def clear_frame():
    """
    Clears all widgets from the root frame.
    """
    for widget in root.winfo_children():
        widget.destroy()


def show_frame(frame_function):
    global navigation_stack, is_going_back
    clear_frame()
    if not is_going_back:
        navigation_stack.append(lambda: frame_function(root))
    frame_function()
    is_going_back = False  # Reset the flag


def go_back(main_frame):
    global navigation_stack, is_going_back
    if len(navigation_stack) > 1:
        navigation_stack.pop()  # Remove current frame
        is_going_back = True
        # Clear the main_frame before displaying the previous frame
        for widget in main_frame.winfo_children():
            widget.destroy()
        navigation_stack[-1]()   # Show the previous frame
        is_going_back = False
    else:
        print("No previous frame available")  # If there's no previous frame, stay on the current frame


def logout():
    global navigation_stack, is_going_back
    navigation_stack = []
    is_going_back = False
    clear_frame()
    open_login_window()


BG_COLOR = "#333333"
BTN_COLOR = "#4a7a8c"
TEXT_FONT = ("Arial", 12)
LABEL_FONT = ("Helvetica", 12, "bold")
ENTRY_FONT = ("Arial", 12)
BTN_FONT = ("Helvetica", 12, "bold")

# Global stack to keep track of navigation
navigation_stack = []
# Global variable to indicate if we are going back
is_going_back = False

# Main application window
root = tk.Tk()
root.geometry("800x700")
# Start RabbitMQ consumer in a separate thread
threading.Thread(target=start_consumer, daemon=True).start()
# Open Initial window
open_login_window()
root.mainloop()
