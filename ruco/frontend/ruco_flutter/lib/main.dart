import 'package:flutter/material.dart';
import 'package:localstorage/localstorage.dart';
import 'package:ruco_flutter/constant/routes.dart';
import 'package:ruco_flutter/pages/bulk-collection/bulk_collection_items_page.dart';
import 'package:ruco_flutter/pages/bulk-collection/bulk_collection_collect_page.dart';
import 'package:ruco_flutter/pages/certficate/certificate.dart';
import 'package:ruco_flutter/pages/collection/collection_details.dart';
import 'package:ruco_flutter/pages/collection/collection_list.dart';
import 'package:ruco_flutter/pages/auth/login.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await initLocalStorage();
  localStorage.removeItem('user');
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: Routes.login,
      routes: {
        Routes.login: (context) => Login(),
        Routes.collectionList: (context) => CollectionListPage(),
        Routes.collectionDetail: (context) => CollectionDetailPage(),
        Routes.certificate: (context) => CertificatePage(),
        Routes.bulkCollectionList: (context) => BulkCollectionItemsPage(),
        Routes.bulkCollectionCollect: (context) => BulkCollectionCollectPage(),
      },
    );
  }
}
