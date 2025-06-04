from flask import Flask, request, jsonify, render_template
from flask_pymongo import PyMongo
from bson.objectid import ObjectId
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi
from dotenv import load_dotenv
from datetime import datetime
from flask_cors import CORS
import os
import traceback

app = Flask(__name__)

CORS(app)

# Load environment variables
load_dotenv()
#  ----------NOTE----------
# Provide your MONGO_URI (Connection String) here: You get that when you first create cluster, connect to cluster etc.; looks like this:
# mongodb+srv://<database_user_username>:<database_user_password>@cluster0.lgrhnze.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
# Don't forget to add your database name that you want to be created when first running the code: Here it is -> ----|| notes_db ||------ in MONGO_URI below.
# app.config["MONGO_URI"] = "mongodb+srv://<database_user_username>:<database_user_password>@cluster0.lgrhnze.mongodb.net/<your_database_name>?retryWrites=true&w=majority&appName=Cluster0"

# ----------------------------------------------
# Change deakintor, TCS9MPoG9IyDhI2H and notes_db from here to your database username, database user password and your database name [this URI won't work, its deleted]
app.config["MONGO_URI"] = "mongodb+srv://s224384709:G0LdyJAgkMAiMH4E@cluster0.zs6ulga.mongodb.net/quiz_app?retryWrites=true&w=majority&appName=Cluster0"
if not app.config["MONGO_URI"]:
    raise ValueError("MONGO_URI environment variable not set")
mongo = PyMongo(app)

# MongoClient for ping test
client = MongoClient(app.config["MONGO_URI"], server_api=ServerApi('1'))

# For landing page
@app.route("/", methods=["GET"])
def welcome_page():
    return render_template("welcome.html")

# Route to confirm MongoDB Atlas connection
@app.route("/ping", methods=["GET"])
def ping_mongodb():
    try:
        client.admin.command('ping')
        return jsonify({"message": "Successfully connected to MongoDB Atlas!"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# Notes page rendering template
@app.route("/notes-page", methods=["GET"])
def notes_page():
    try:
        notes = mongo.db.notes.find()
        return render_template("index.html", notes=notes)
    except Exception as e:
        return jsonify({"error": f"Failed to fetch notes: {str(e)}"}), 500

# Get List of Notes
@app.route("/notes", methods=["GET"])
def get_notes():
    try:
        notes = list(mongo.db.notes.find())
        for note in notes:
            note["_id"] = str(note["_id"])
        return jsonify({"notes": notes}), 200
    except Exception as e:
        return jsonify({"error": f"Failed to fetch notes: {str(e)}"}), 500

# Add a note
@app.route("/notes", methods=["POST"])
def create_note():
    try:
        note_data = {
            "title": request.json["title"],
            "content": request.json["content"],
            "category": request.json["category"]
        }
        result = mongo.db.notes.insert_one(note_data)
        note_data["_id"] = str(result.inserted_id)
        return jsonify({"message": "Note added successfully", "note": note_data}), 201
    except Exception as e:
        return jsonify({"error": f"Failed to create note: {str(e)}"}), 400

# Update a Note
@app.route("/notes/<id>", methods=["PUT"])
def update_note(id):
    try:
        note_data = {
            "title": request.json["title"],
            "content": request.json["content"],
            "category": request.json["category"]
        }
        result = mongo.db.notes.update_one(
            {"_id": ObjectId(id)},
            {"$set": note_data}
        )
        if result.modified_count:
            note_data["_id"] = id
            return jsonify({"message": "Note updated successfully", "note": note_data}), 200
        return jsonify({"error": "Note not found"}), 404
    except Exception as e:
        return jsonify({"error": f"Failed to update note: {str(e)}"}), 400

# Delte note
@app.route("/notes/<id>", methods=["DELETE"])
def delete_note(id):
    try:
        result = mongo.db.notes.delete_one({"_id": ObjectId(id)})
        if result.deleted_count:
            return jsonify({"message": "Note deleted successfully"}), 200
        return jsonify({"error": "Note not found"}), 404
    except Exception as e:
        return jsonify({"error": f"Failed to delete note: {str(e)}"}), 400

# Quiz record   
@app.route("/submit-quiz", methods=["POST"])
def submit_quiz():
    try:
        data = request.json
        print("Received data:", data)

        required_fields = ["username", "question","options", "useranswer","correctanswer"]
        if not all(field in data for field in required_fields):
            return jsonify({"error": "Missing required fields"}), 400

        # organize data
        record = {
            "username": data["username"],
            "question": data["question"],
            "options": data.get("options"),
            "useranswer": data["useranswer"],
            "correctanswer":data["correctanswer"],
            "submitted_at": datetime.utcnow()
        }

        # insert MongoDB
        mongo.db.quiz_records.insert_one(record)

        return jsonify({"message": "Quiz record submitted successfully!"}), 201

    except Exception as e:
        print("Error in /submit-quiz:", e)
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500
    
@app.route("/quiz-history/<username>", methods=["GET"])
def get_quiz_history(username):
    try:
        records = list(mongo.db.quiz_records.find({"username": username}))
        for r in records:
            r["_id"] = str(r["_id"])
            r["submitted_at"] = r["submitted_at"].isoformat()
        return jsonify({"records": records}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    
@app.route('/getQuizRecords', methods=['GET'])
def get_quiz_records():
    username = request.args.get('username')
    records = mongo.db.quiz_records.find({"username": username})
    result = []
    for r in records:
        result.append({
            "question": r.get("question"),
            "options": r.get("options", []),
            "useranswer": r.get("useranswer"),
            "correctanswer": r.get("correctanswer")
        })
    return jsonify(result)

@app.route("/share-latest-result", methods=["GET"])
def share_latest_result():
    username = request.args.get("username")
    if not username:
        return "Missing username", 400

    try:
        record = mongo.db.quiz_records.find_one(
            {"username": username},
            sort=[("submitted_at", -1)]
        )

        if not record:
            return "No quiz record found for this user", 404

        return render_template("shared_result.html", record=record)
    except Exception as e:
        return f"Error: {str(e)}", 500


if __name__ == "__main__":
    app.run(port=5000, host="0.0.0.0")