import React, { useState, useEffect } from "react";
import toast from "react-hot-toast";
import { sendEmail } from "../services/email.service";
import { Editor } from "@tinymce/tinymce-react";
import { useRef } from 'react';


function EmailSender() {
    const [emailData, setEmailData] = useState({
        to: "",
        subject: "",
        message: "",
    });

    const [sending, setSending] = useState(false);
    const [isDarkMode, setIsDarkMode] = useState(false);

    const editorRef = useRef(null);

    function handleFieldChange(event, name) {
        setEmailData({ ...emailData, [name]: event.target.value });
    }

    async function handleSubmit(event) {
        event.preventDefault();

        if (emailData.to === "" || emailData.subject === "" || emailData.message === "") {
            toast.error("All fields are required.");
            return;
        }

        // Send mail using API
        try {
            setSending(true);
            await sendEmail(emailData);
            toast.success("Email sent successfully.");

            // Reset form after successful email send
            setEmailData({
                to: "",
                subject: "",
                message: "",
            });

            editorRef.current.setContent("")
        } catch (error) {
            console.error(error);
            toast.error("Email not sent.");
        } finally {
            setSending(false);
        }

        console.log(emailData);
    }

    function handleReset() {
        setEmailData({ to: "", subject: "", message: "" });
    }

    // Toggle the theme between light and dark mode
    function toggleTheme() {
        setIsDarkMode(!isDarkMode);
    }

    // Apply the theme to the body class dynamically
    useEffect(() => {
        if (isDarkMode) {
            document.body.classList.add("dark");
        } else {
            document.body.classList.remove("dark");
        }
    }, [isDarkMode]);

    return (
        <div
            className={`w-full min-h-screen flex justify-center items-center transition-all duration-500 ${
                isDarkMode ? "bg-gray-900 text-white" : "bg-gray-100 text-gray-900"
            }`}
        >
            <div
                className={`email_card md:w-1/2 w-full mx-4 p-4 -mt-10 rounded-lg border shadow ${
                    isDarkMode ? "bg-gray-800 border-gray-700" : "bg-white border-gray-300"
                }`}
            >
                <h1 className="text-3xl">Email Sender</h1>
                <p>Send an email to your favorite person with your own app...</p>

                <form onSubmit={handleSubmit}>
                    {/* To */}
                    <div className="input_field">
                        <label htmlFor="email-input" className="mb-2  text-sm font-medium mt-4">
                            To
                        </label>
                        <input
                            value={emailData.to}
                            onChange={(event) => handleFieldChange(event, "to")}
                            type="text"
                            id="email-input"
                            className={`block w-full p-4 text-sm border rounded-lg focus:ring-blue-500 focus:border-blue-500 focus:outline-none ${
                                isDarkMode
                                    ? "text-gray-200 bg-gray-700 border-gray-600"
                                    : "text-gray-900 bg-gray-100 border-gray-300"
                            }`}
                            placeholder="Enter email address..."
                        />
                    </div>

                    {/* Subject */}
                    <div className="input_field">
                        <label htmlFor="subject-input" className="mb-2 text-sm font-medium mt-4">
                            Subject
                        </label>
                        <input
                            value={emailData.subject}
                            onChange={(event) => handleFieldChange(event, "subject")}
                            type="text"
                            id="subject-input"
                            className={`block w-full p-4 text-sm border rounded-lg focus:ring-blue-500 focus:border-blue-500 focus:outline-none ${
                                isDarkMode
                                    ? "text-gray-200 bg-gray-700 border-gray-600"
                                    : "text-gray-900 bg-gray-100 border-gray-300"
                            }`}
                            placeholder="Enter subject..."
                        />
                    </div>

                    {/* Message */}
                    <div className="form_field mt-4">
                        <label
                            htmlFor="message-input"
                            className="block mb-2 mt-2 text-sm font-medium"
                        >
                            Your message
                        </label>
                        {/* <textarea
                            value={emailData.message}
                            onChange={(event) => handleFieldChange(event, "message")}
                            id="message-input"
                            rows="10"
                            className={`block p-2.5 w-full text-sm border rounded-lg focus:ring-blue-500 focus:border-blue-500 focus:outline-none ${
                                isDarkMode
                                    ? "text-gray-200 bg-gray-700 border-gray-600"
                                    : "text-gray-900 bg-gray-50 border-gray-300"
                            }`}
                            placeholder="Write your thoughts here..."
                        ></textarea> */}

                        <Editor 

                            onEditorChange={event=>{
                                setEmailData({...emailData,'message':editorRef.current.getContent()});
                            }}

                            onInit={(evt, editor) => editorRef.current = editor}
                            apiKey="3olmjvmoc2ncwu6ha01n8wiiw1e1hfc08h42i5055e4uhl91"
                            // value={emailData.message}
                            
                            init={{
                                plugins: [
                                  // Core editing features
                                  'anchor', 'autolink', 'charmap', 'codesample', 'emoticons', 'image', 'link', 'lists', 'media', 'searchreplace', 'table', 'visualblocks', 'wordcount',
                                  // Your account includes a free trial of TinyMCE premium features
                                  // Try the most popular premium features until Oct 21, 2024:
                                  'checklist', 'mediaembed', 'casechange', 'export', 'formatpainter', 'pageembed', 'a11ychecker', 'tinymcespellchecker', 'permanentpen', 'powerpaste', 'advtable', 'advcode', 'editimage', 'advtemplate', 'ai', 'mentions', 'tinycomments', 'tableofcontents', 'footnotes', 'mergetags', 'autocorrect', 'typography', 'inlinecss', 'markdown',
                                ],
                                toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
                                tinycomments_mode: 'embedded',
                                branding: false,
                                tinycomments_author: 'Kiran',
                                mergetags_list: [
                                  { value: 'First.Name', title: 'Kiran Bhad' },
                                  { value: 'Email', title: 'bhadkiran1804@gmail.com' },
                                ],
                                ai_request: (request, respondWith) => respondWith.string(() => Promise.reject('See docs to implement AI Assistant')),
                              }}
                        />

                    </div>

                    {/* Loader */}
                    {sending && (
                        <div className="loader flex flex-col gap-2 items-center justify-center mt-4">
                            <div role="status">
                                <svg
                                    aria-hidden="true"
                                    className="w-8 h-8 text-gray-200 animate-spin fill-blue-600"
                                    viewBox="0 0 100 101"
                                    fill="none"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                    <path
                                        d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                                        fill="currentColor"
                                    />
                                    <path
                                        d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                                        fill="currentFill"
                                    />
                                </svg>
                                <span className="sr-only">Loading...</span>
                            </div>
                            <p>Sending email...</p>
                        </div>
                    )}

                    {/* Buttons */}
                    <div className="button-container flex justify-center gap-3 mt-4">
                        <button
                            type="submit"
                            disabled={sending}
                            className={`text-white px-2 py-2 rounded ${
                                sending
                                    ? "bg-blue-300 cursor-not-allowed"
                                    : "bg-blue-700 hover:bg-blue-900"
                            }`}
                        >
                            {sending ? "Sending..." : "Send Email"}
                        </button>
                        <button
                            type="button"
                            onClick={handleReset}
                            className="hover:bg-red-700 text-white bg-gray-700 px-2 py-2 rounded"
                        >
                            Reset
                        </button>
                    </div>
                </form>

                {/* Theme Toggle Icon */}
                <div className="absolute top-4 right-4">
                    <button onClick={toggleTheme}>
                        <i
                            className={`fas ${
                                isDarkMode ? "fa-sun text-yellow-400" : "fa-moon text-gray-900"
                            } text-2xl`}
                        ></i>
                    </button>
                </div>
            </div>
        </div>
    );
}

export default EmailSender;
