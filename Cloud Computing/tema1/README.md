# Node.js web server without frameworks
 - searches an image with the specified tags using the [Flickr API](https://www.flickr.com/services/api/flickr.photos.search.html)  
 - generates hashtags for the searched image using the [Imagga API](https://docs.imagga.com/#tags)  
 - tweets the searched image with the generated hashtags using the [Twitter API](https://developer.twitter.com/en/docs/twitter-api/v1/tweets/post-and-engage/api-reference/post-statuses-update)  
 - logs requests (headers, method, body) and responses (status code, status message, headers, body)  
 - calculates metrics (requests per minute, average latency, number of GET or POST requests, number of responses with 200 or 400 status code)
# Web interface
 - sends parallel tweet requests  
![Web interface](https://github.com/georgiana-ojoc/UniversityAssignments/blob/master/Cloud%20Computing/tema1/web-interface.gif)
