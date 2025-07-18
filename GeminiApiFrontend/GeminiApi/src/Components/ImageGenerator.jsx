import React, { useState } from "react";

function ImageGenerator() {
    const [prompt, setPrompt] = useState('');
    const [imageUrl, setImageUrl] = useState('');

    const generateImage = async () => {
        try {
            // Encode the prompt for URL safety
            const encodedPrompt = encodeURIComponent(prompt);
            
            const response = await fetch(
                `http://localhost:8080/api/ask?payload=${encodedPrompt}`
            );
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            // Get the URL as plain text
            const url = await response.text();
            
            // Validate it's a URL
            if (!url.startsWith('http')) {
                throw new Error(`Invalid URL received: ${url}`);
            }
            
            console.log("Generated image URL:", url);
            setImageUrl(url);
            
        } catch (error) {
            console.error("Error generating image:", error);
            alert(`Error: ${error.message}`);
        }
    };

    return (
        <div className="tab-content">
            <div className="image-generator">
                <h2>Generate Image</h2>
                
                <input
                    type="text"
                    value={prompt}
                    onChange={(e) => setPrompt(e.target.value)}
                    placeholder="Describe the image you want"
                />
                
                <button onClick={generateImage}>Generate Image</button>
                
                <div className="image-preview">
                    {imageUrl ? (
                        <img 
                            src={imageUrl} 
                            alt="Generated content" 
                            onError={(e) => {
                                console.error("Error loading image:", imageUrl);
                                e.target.style.display = 'none';
                            }}
                        />
                    ) : (
                        <div className="empty-image-slot">Image will appear here</div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default ImageGenerator;