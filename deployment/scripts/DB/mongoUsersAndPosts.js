db.user.insertMany([
    {
        "_id": ObjectId("5e62492e0857af69dd5e32a9"),
        "firstName": "Gabi",
        "lastName": "R",
        "userName": "arrnaux",
        "email": "gabriel.raileanu1@xwiki.com",
        "password": "7RH4v2oRIEhxoP7xJpxpMvwcsa7+ReROh9UoH7IFYSl2xQW6SaOo7bXvIEKZMhc1p49uxGB1+T6G/jearWIgFA==",
        "_class": "com.arrnaux.demetria.core.models.userAccount.SNUser"
    },
    {
        "_id": ObjectId("5e62492e0857af69dd5e32aa"),
        "firstName": "Cris",
        "lastName": "R",
        "userName": "cris",
        "email": "raileanu.cristina91@gmail.com",
        "password": "7RH4v2oRIEhxoP7xJpxpMvwcsa7+ReROh9UoH7IFYSl2xQW6SaOo7bXvIEKZMhc1p49uxGB1+T6G/jearWIgFA==",
        "_class": "com.arrnaux.demetria.core.models.userAccount.SNUser"
    },
    {
        "_id": ObjectId("5e62492e0857af69dd5e32ab"),
        "firstName": "Mihai",
        "lastName": "Georgescu",
        "userName": "mihai",
        "email": "mihai.georgescu@gmail.com",
        "password": "7RH4v2oRIEhxoP7xJpxpMvwcsa7+ReROh9UoH7IFYSl2xQW6SaOo7bXvIEKZMhc1p49uxGB1+T6G/jearWIgFA==",
        "_class": "com.arrnaux.demetria.core.models.userAccount.SNUser"
    },
    {
        "_id": ObjectId("5e62492e0857af69dd5e32ac"),
        "firstName": "John",
        "lastName": "hatz",
        "userName": "john",
        "email": "john.hatz@gmail.com",
        "password": "7RH4v2oRIEhxoP7xJpxpMvwcsa7+ReROh9UoH7IFYSl2xQW6SaOo7bXvIEKZMhc1p49uxGB1+T6G/jearWIgFA==",
        "_class": "com.arrnaux.demetria.core.models.userAccount.SNUser"
    },
    {
        "_id": ObjectId("5e62492e0857af69dd5e32ad"),
        "firstName": "George",
        "lastName": "Mihaila",
        "userName": "george",
        "email": "george.mihaila@gmail.com",
        "password": "7RH4v2oRIEhxoP7xJpxpMvwcsa7+ReROh9UoH7IFYSl2xQW6SaOo7bXvIEKZMhc1p49uxGB1+T6G/jearWIgFA==",
        "_class": "com.arrnaux.demetria.core.models.userAccount.SNUser"
    }
])

db.post.insertMany([
    {
        "_id": ObjectId("5e6256af31a3e643a0508148"),
        "ownerId": "5e62492e0857af69dd5e32aa",
        "content": "Private Cris.",
        "visibility": "PRIVATE",
        "creationDate": ISODate("2020-03-06T13:57:03.266Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e6256b231a3e643a0508149"),
        "ownerId": "5e62492e0857af69dd5e32aa",
        "content": "Private Cris 2.",
        "visibility": "PRIVATE",
        "creationDate": ISODate("2020-03-06T13:57:06.875Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e6256c231a3e643a050814b"),
        "ownerId": "5e62492e0857af69dd5e32aa",
        "content": "Public Cris 1",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:57:22.274Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e6256e131a3e643a050814c"),
        "ownerId": "5e62492e0857af69dd5e32a9",
        "content": "Postare privata arrnaux 1",
        "visibility": "PRIVATE",
        "creationDate": ISODate("2020-03-06T13:57:53.114Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e6256e931a3e643a050814d"),
        "ownerId": "5e62492e0857af69dd5e32a9",
        "content": "Postare publica Arrnaux 1",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:58:01.861Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e6256fa31a3e643a050814e"),
        "ownerId": "5e62492e0857af69dd5e32ac",
        "content": "Postare hatz publica",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:58:18.691Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e62572631a3e643a050814f"),
        "ownerId": "5e62492e0857af69dd5e32ab",
        "content": "Postare publica mihai 1",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:59:02.716Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e62572b31a3e643a0508150"),
        "ownerId": "5e62492e0857af69dd5e32ab",
        "content": "Postare publica 2",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:59:07.808Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e62572e31a3e643a0508151"),
        "ownerId": "5e62492e0857af69dd5e32ab",
        "content": "private 1",
        "visibility": "PRIVATE",
        "creationDate": ISODate("2020-03-06T13:59:10.577Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e62575231a3e643a0508152"),
        "ownerId": "5e62492e0857af69dd5e32ad",
        "content": "Postare publica ",
        "visibility": "PUBLIC",
        "creationDate": ISODate("2020-03-06T13:59:46.114Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    },
    {
        "_id": ObjectId("5e62575731a3e643a0508153"),
        "ownerId": "5e62492e0857af69dd5e32ad",
        "content": "Postare nepublica",
        "visibility": "PRIVATE",
        "creationDate": ISODate("2020-03-06T13:59:51.612Z"),
        "averageRank": 0,
        "_class": "com.arrnaux.demetria.core.models.userPost.SNPost"
    }
]);