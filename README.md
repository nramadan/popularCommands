# popularCommands
 Finds the most popular speach commands at state and national level
 
# To get started follow these steps:

# 1) Install Solr 8.6
Install Solr on Windows per the instructions in the Document “Installating Solr on Windows.docx”.

# 2) Run Solr
PS H:\thirdparty\solr-8.6.1\bin> .\solr start

Java HotSpot(TM) 64-Bit Server VM warning: JVM cannot use large page memory because it does not have enough privilege to lock pages in memory.
Waiting up to 30 to see Solr running on port 8983
Started Solr server on port 8983. Happy searching!


# 3) Create stateCommands Core
PS H:\thirdparty\solr-8.6.1\bin>.\solr create -c stateCommands

WARNING: Using _default configset with data driven schema functionality. NOT RECOMMENDED for production use.
         To turn off: bin\solr config -c stateCommands -p 8983 -action set-user-property -property update.autoCreateFields -value false

Created new core 'stateCommands'

# 4) Change the \server\solr\stateCommands\conf\managed-schema 

Make the following changes:

# 4 a) add new fieldType text_gen_lower_case
<fieldType name="text_gen_lower_case" class="solr.TextField" positionIncrementGap="100">
      <analyzer type="index">
        <tokenizer class="solr.KeywordTokenizerFactory"/>
        <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
        <filter class="solr.CapitalizationFilterFactory" onlyFirstWord="false"/>
      </analyzer>
      <analyzer type="query">
        <tokenizer class="solr.KeywordTokenizerFactory"/>
        <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
        <filter class="solr.CapitalizationFilterFactory"  onlyFirstWord="false"/>
      </analyzer>
</fieldType>

# 4 b) Assign fieldType to stateCommands indexed fields
  <field name="command" type="text_gen_lower_case"/>
  <field name="speaker" type="text_gen_lower_case"/>
  <field name="state" type="text_gen_lower_case"/>
  
# The actual managed-schema with all these changes above is in the repository.  Just copy it.

# 5) Run the commandApp Spring Boot Application

One option is to Import the application maven project into eclipse and run it as followings:
 
 - Run the spring boot app, commandApp

- Right click the maven project parent folder and click run java application then select “commandApp”.

# 6) load up the stateCommands core with initial data (as provided by problem description).
To do this run the following POST request from Postman:
http://localhost:8080/commands 
with the following as the requestion body:

{
	"Alabama": [
		{
			"speaker": "Fred Zhang",
			"command": "CNN"
		},
		{
			"speaker": "Fred Zhang",
			"command": "NBC"
		},
		{
			"speaker": "Fred Zhang",
			"command": "CNN"
		}
	],
	"florida": [
		{
			"speaker": "Thomas Brown",
			"command": "Show me movies"
		},
		{
			"speaker": " Alisha Brown",
			"command": " Stranger Things"
		},
		{
			"speaker": "Marcus Brown",
			"command": "Game of Thrones"
		},
		{
			"speaker": "Missy Brown",
			"command": "Turn off the TV"
		},
		{
			"speaker": "Missy Brown",
			"command": "Turn off the TV"
		}
	],
	"maryland": [
		{
			"speaker": "Thomas Black",
			"command": "Show me comedies"
		},
		{
			"speaker": " Alisha Black",
			"command": "Game of thrones"
		},
		{
			"speaker": "Marcus Black",
			"command": "Game of THrones"
		},
		{
			"speaker": "Missy Black",
			"command": "Game of Thrones "
		},
		{
			"speaker": "Missy Black",
			"command": "Turn off the TV"
		}
	]
}




# Here are the underlying queries in the application
 
# 1) Nationally popular command query
The following query grabs the most popular 3 commands at the national level.
http://localhost:8983/solr/stateCommands/select?q=*:*&wt=json&facet=true&facet.field=command&facet.limit=3

# 2) State popular command query
The query will grab the state and command that appears first since that is the the most popular command.

http://localhost:8983/solr/stateCommands/select?q=*:*&wt=json&facet=true&facet.pivot=state,command 

# 3) Stop and Start processing time query
To get the process stop time and start time, we will grab the created date for the populare command in that state and look it up in descending order
of its created date.  Then grab the first and last for the stop and start time respectively.

http://localhost:8983/solr/stateCommands/select?q=state:%22florida%22%20AND%20speaker:%22missy%20Brown%22&sort=createdDt%20asc


