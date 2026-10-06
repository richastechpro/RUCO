import 'package:flutter/material.dart';
import 'package:logger/logger.dart';
import 'package:ruco_flutter/client/dio-client.dart';
import 'package:ruco_flutter/constant/environment.dart';
import 'package:ruco_flutter/pages/utils/show_snackbar.dart';
import 'package:ruco_flutter/pages/utils/side_menu_header.dart' show UserAvatar;

import '../../constant/routes.dart';

class CollectionListPage extends StatefulWidget {
  const CollectionListPage({super.key});

  @override
  State<CollectionListPage> createState() => _CollectionListPageState();
}

class _CollectionListPageState extends State<CollectionListPage> {
  int pageSize = 5;
  int offset = 0;
  bool isLoading = false;
  bool hasMore = true;
  bool onStart = true;
  List<dynamic> collections = [];
  final ScrollController _scrollController = ScrollController();
  final logger = Logger();
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  Future<void> fetchData() async {
    setState(() => isLoading = true);

    try {
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.get(
        '$url/collection/assigned/$pageSize/$offset',
      );
      final List<dynamic> fetchedData = res.data['content'];
      final bool isLast = res.data['last'];
      setState(() {
        offset += 1;
        collections.addAll(fetchedData);
        isLoading = false;
        hasMore = !isLast;
        onStart = false;
      });
    } catch (e) {
      logger.e('Error fetching data: $e');
      setState(() {
        isLoading = false;
        onStart = false;
      });
    } finally {
      setState(() {
        isLoading = false;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    fetchData();
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
              _scrollController.position.maxScrollExtent &&
          !isLoading &&
          hasMore) {
        fetchData();
      }
    });
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  Future<void> _downloadCertificate() async {
    SnackBarHelper.show(context, {'msg': 'Downloading certificate...'});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        backgroundColor: const Color.fromARGB(255, 115, 101, 255),
        title: const Text(
          'Collection List',
          style: TextStyle(color: Colors.white),
        ),
        leading: IconButton(
          icon: const Icon(Icons.menu, color: Colors.white),
          onPressed: () {
            _scaffoldKey.currentState?.openDrawer();
          },
        ),
        // actions: [
        //   IconButton(
        //     icon: const Icon(Icons.add, color: Colors.white, size: 30),
        //     onPressed: () {
        //       Navigator.push(
        //         context,
        //         MaterialPageRoute(
        //           builder: (context) => const NewCollectionPage(),
        //         ),
        //       );
        //     },
        //   ),
        // ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            const DrawerHeader(
              decoration: BoxDecoration(color: Colors.lightBlue),
              child: UserAvatar(),
            ),
            ListTile(
              title: const Text(
                'Logout',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              onTap: () {
                Navigator.pushNamedAndRemoveUntil(
                  context,
                  Routes.login,
                  (Route<dynamic> route) => false,
                );
              },
            ),
          ],
        ),
      ),
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/background_new.png'),
            fit: BoxFit.cover,
          ),
        ),
        child:
            isLoading && onStart
                ? const Center(
                  child: CircularProgressIndicator(color: Colors.blueAccent),
                )
                : RefreshIndicator(
                  onRefresh: (() async {
                    setState(() {
                      offset = 0;
                      collections.clear();
                      pageSize = 5;
                      hasMore = true;
                      onStart = true;
                    });
                    await fetchData();
                  }),
                  child: ListView.builder(
                    physics: const AlwaysScrollableScrollPhysics(),
                    controller: _scrollController,
                    padding: const EdgeInsets.all(16),
                    itemCount: collections.length + (hasMore ? 1 : 0),
                    itemBuilder: (context, index) {
                      if (index < collections.length) {
                        final item = collections[index];
                        final isCollected =
                            item['collectedStatus'] == 'Collected';
                        final type = item['type'];
                        return Card(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(28),
                          ),
                          margin: const EdgeInsets.only(bottom: 24),
                          child: Padding(
                            padding: const EdgeInsets.all(20),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Align(
                                  alignment: Alignment.center,
                                  child: Container(
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 16,
                                      vertical: 4,
                                    ),
                                    decoration: BoxDecoration(
                                      color:
                                          isCollected
                                              ? const Color(0xFFC7E76E)
                                              : const Color(0xFFF9B5B5),
                                      borderRadius: BorderRadius.circular(20),
                                    ),
                                    child: Text(
                                      item['collectedStatus'],
                                      style: const TextStyle(
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ),
                                const SizedBox(height: 16),
                                Text(
                                  item['collectionName']!,
                                  style: const TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 18,
                                  ),
                                ),
                                const SizedBox(height: 8),
                                Row(
                                  children: [
                                    const Icon(Icons.calendar_month),
                                    const SizedBox(width: 6),
                                    Text(item['requestDate']!),
                                    const Spacer(),
                                    const Icon(Icons.inventory_2_outlined),
                                    const SizedBox(width: 6),
                                    Text(item['assigneStatus']!),
                                  ],
                                ),
                                const SizedBox(height: 16),
                                ElevatedButton(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor:
                                        isCollected
                                            ? const Color.fromARGB(
                                              255,
                                              0,
                                              85,
                                              36,
                                            )
                                            : const Color.fromARGB(
                                              255,
                                              115,
                                              101,
                                              255,
                                            ),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(32),
                                    ),
                                    minimumSize: const Size.fromHeight(48),
                                  ),
                                  onPressed: () async {
                                    if (mounted) {
                                      isCollected
                                          ? _downloadCertificate()
                                          : type == 'SINGLE'
                                          ? await Navigator.pushNamed(
                                            context,
                                            Routes.collectionDetail,
                                            arguments: {'id': item['id']},
                                          )
                                          : await Navigator.pushNamed(
                                            context,
                                            Routes.bulkCollectionList,
                                            arguments: {'id': item['id']},
                                          );
                                    }

                                    return;
                                  },
                                  child:
                                      isCollected
                                          ? const Text(
                                            'Download Certificate',
                                            style: TextStyle(
                                              fontWeight: FontWeight.bold,
                                              letterSpacing: 1.2,
                                              color: Colors.white,
                                            ),
                                          )
                                          : const Text(
                                            'Collect',
                                            style: TextStyle(
                                              fontWeight: FontWeight.bold,
                                              letterSpacing: 1.2,
                                              color: Colors.white,
                                            ),
                                          ),
                                ),
                              ],
                            ),
                          ),
                        );
                      } else {
                        return const Padding(
                          padding: EdgeInsets.symmetric(vertical: 16),
                          child: Center(
                            child: CircularProgressIndicator(
                              color: Colors.blue,
                            ),
                          ),
                        );
                      }
                    },
                  ),
                ),
      ),
    );
  }
}
