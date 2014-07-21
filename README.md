Simple application using MongoDB with Spring Data and Spring Rest Controllers

MongoDB and Spring Data
- Basics (created with MongoDB 2.6.3)
- User defined queries in repository interface
- JSR 303 validation with annotations (on Place)
- Indexes (default, single-field)
- Embedded documents with ID
- AbstractMongoEventListener (AfterLoadListener for statistics, BeforeSaveValidator for validation)

Spring Rest
- Basics of @RestController
- @PathVariable, @RequestParam, @RequestBody
- Input data validation with @Valid, @InitBinder and PlaceDtoValidator
- Error handler (RestErrorHandler)
- Returns json. Pay attention on usage of com.fasterxml.jackson.core v.2+ instead of org.codehaus.jackson and RequestMappingHandlerAdapter definition in the context xml
- @ResponseBody and @ResponseEntity
- Localization


General:
Example of response body for initial data population (addressDTOs list and addressAlias field are optional):
 {
     "name": "place4",
     "notes": "place4 notes",
     "addressDTOs": [
         {
           "addressAlias": "Central location",
           "address": "Street 4, 2",
           "metro": "Center-4",
           "comment": "My first address"
         }
     ]
 }
