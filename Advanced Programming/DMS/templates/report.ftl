<html>
<head>
    <title>${title}</title>
</head>
<body>
<h1> Catalog ${name} found at ${path}</h1>
<p>That has all this documents:</p>
<ul>
    <#list documents as document>
        <li>${document_index + 1}.${document.name} with id = ${document.id} found at ${document.location} that has all this tags:
            <ul>
                <#list document.tags?keys as key>
                    ${key}: ${document.tags[key]}<br>
                </#list>
            </ul>
        </li>
    </#list>
</ul>
</body>
</html>