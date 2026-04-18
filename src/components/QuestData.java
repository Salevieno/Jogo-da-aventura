package components;

import java.util.Map;

public abstract class QuestData
{
    public static void create()
    {
        new Quest(0, true, Map.of(0, 10, 1, 5), Map.of(), 20, 150, Map.of());
        new Quest(1, true, Map.of(5, 10, 6, 5), Map.of(), 20, 150, Map.of());
        new Quest(2, true, Map.of(10, 10, 11, 5), Map.of(), 20, 150, Map.of());
        new Quest(3, true, Map.of(15, 10, 16, 5), Map.of(), 20, 150, Map.of());
        new Quest(4, true, Map.of(20, 10, 21, 5), Map.of(), 20, 150, Map.of());
        new Quest(5, false, Map.of(), Map.of(1850, 1, 1851, 1, 1852, 1, 1853, 1, 1854, 1), 0, 0, Map.of(436, 1, 437, 1, 438, 1));
        new Quest(6, true, Map.of(120, 5, 121, 5, 122, 5), Map.of(1950, 1, 1951, 1), 2000, 2500, Map.of());
        new Quest(7, false, Map.of(), Map.of(1800, 1, 1801, 1, 1802, 1, 1803, 1, 1804, 1), 0, 0, Map.of(401, 1));
        new Quest(8, false, Map.of(), Map.of(1810, 1, 1811, 1, 1812, 1, 1813, 1, 1814, 1), 0, 0, Map.of(412, 1));
        new Quest(9, false, Map.of(), Map.of(1800, 1, 1801, 1, 1802, 1, 1803, 1, 1804, 1), 0, 0, Map.of(401, 1));
        new Quest(10, false, Map.of(), Map.of(1810, 1, 1811, 1, 1812, 1, 1813, 1, 1814, 1), 0, 0, Map.of(412, 1));
        new Quest(11, false, Map.of(), Map.of(1810, 1, 1811, 1, 1812, 1, 1813, 1, 1814, 1), 0, 0, Map.of(412, 1));
        new Quest(12, false, Map.of(), Map.of(1855, 1, 1856, 1, 1857, 1, 1858, 1, 1859, 1), 100, -1, Map.of(415, 1, 416, 1, 417, 1));
        new Quest(13, true, Map.of(55, 5, 56, 5, 57, 5), Map.of(1952, 1), 1000, 1500, Map.of());
        new Quest(14, false, Map.of(), Map.of(1860, 1, 1861, 1, 1862, 1, 1863, 1, 1864, 1), 0, 0, Map.of(430, 1));
        new Quest(15, true, Map.of(80, 5, 81, 5, 82, 5), Map.of(1953, 1, 1954, 1, 1955, 1), 1500, 2000, Map.of());
        new Quest(16, false, Map.of(108, 3), Map.of(1865, 1, 1866, 1, 1867, 1, 1868, 1, 1869, 1), 0, 0, Map.of(20, 2, 21, 2, 41, 1));
        new Quest(17, true, Map.of(105, 5, 106, 5, 107, 5), Map.of(1956, 1), 2000, 2500, Map.of());
        new Quest(18, false, Map.of(), Map.of(1800, 1, 1801, 1, 1802, 1, 1803, 1, 1804, 1), 0, 0, Map.of(401, 1));
        new Quest(19, false, Map.of(), Map.of(1870, 1, 1871, 1, 1872, 1, 1873, 1, 1874, 1), 0, 0, Map.of());
        new Quest(20, true, Map.of(85, 5, 86, 5, 87, 5), Map.of(1957, 1, 1958, 1, 1959, 1), 1500, 2000, Map.of());
        new Quest(21, false, Map.of(), Map.of(1875, 1, 1876, 1, 1877, 1, 1878, 1, 1879, 1), 0, 0, Map.of(420, 1, 421, 1, 422, 1));
        new Quest(22, true, Map.of(70, 5, 71, 5, 72, 5), Map.of(1960, 1), 1000, 1500, Map.of());
        new Quest(23, false, Map.of(), Map.of(1880, 1, 1881, 1, 1882, 1, 1883, 1, 1884, 1), 0, 0, Map.of(1340, 1));
        new Quest(24, true, Map.of(95, 5, 96, 5, 97, 5), Map.of(1961, 1, 1962, 1), 1500, 2000, Map.of());
        new Quest(25, false, Map.of(), Map.of(1885, 1, 1886, 1, 1887, 1, 1888, 1, 1889, 1), 0, 0, Map.of(419, 1, 420, 1, 421, 1));
        new Quest(26, true, Map.of(50, 5, 51, 5, 52, 5), Map.of(1963, 1), 1000, 1500, Map.of());
        new Quest(27, false, Map.of(), Map.of(1964, 1, 1965, 1), 0, 0, Map.of());
        new Quest(28, true, Map.of(60, 5, 61, 5, 62, 5), Map.of(1966, 1, 1967, 1), 1000, 1500, Map.of());
        new Quest(29, false, Map.of(), Map.of(1805, 1, 1806, 1, 1807, 1, 1808, 1, 1809, 1), 0, 0, Map.of(401, 1));
        new Quest(30, false, Map.of(76, 5, 77, 10, 78, 3), Map.of(1890, 1, 1891, 1, 1892, 1, 1893, 1, 1894, 1), 0, 0, Map.of(128, 2, 129, 2));
        new Quest(31, true, Map.of(75, 5, 76, 5, 77, 5), Map.of(1968, 1), 1500, 2000, Map.of());
        new Quest(32, false, Map.of(103, 3), Map.of(1895, 1, 1896, 1, 1897, 1, 1898, 1, 1899, 1), 0, 0, Map.of(19, 1, 130, 2, 131, 2));
        new Quest(33, true, Map.of(100, 5, 101, 5, 102, 5), Map.of(1969, 1, 1970, 1), 2000, 2500, Map.of());
        new Quest(34, false, Map.of(), Map.of(1810, 1, 1811, 1, 1812, 1, 1813, 1, 1814, 1), 0, 0, Map.of(412, 1));
        new Quest(35, true, Map.of(114, 10), Map.of(1971, 1), -1, 2000, Map.of());
        new Quest(36, true, Map.of(110, 5, 111, 5, 112, 5), Map.of(1972, 1), 2000, 2500, Map.of());
        new Quest(37, false, Map.of(118, 3), Map.of(1900, 1, 1901, 1, 1902, 1, 1903, 1, 1904, 1), 0, 0, Map.of(1380, 2, 1399, 2));
        new Quest(38, true, Map.of(115, 5, 116, 5, 117, 5), Map.of(1973, 1, 1974, 1), 2000, 2500, Map.of());
        new Quest(39, false, Map.of(), Map.of(1975, 1, 1976, 1), 0, 0, Map.of());
        new Quest(40, true, Map.of(90, 5, 91, 5, 92, 5), Map.of(1977, 1), 1500, 2000, Map.of());
        new Quest(41, false, Map.of(), Map.of(1800, 1, 1801, 1, 1802, 1, 1803, 1, 1804, 1), 0, 0, Map.of(401, 1));
        new Quest(42, false, Map.of(), Map.of(1978, 1, 1979, 1), 0, 0, Map.of());
        new Quest(43, true, Map.of(65, 5, 66, 5, 67, 5), Map.of(1980, 1), 1000, 1500, Map.of());
        new Quest(44, false, Map.of(), Map.of(1810, 1, 1811, 1, 1812, 1, 1813, 1, 1814, 1), 0, 0, Map.of(412, 1));
        new Quest(45, true, Map.of(125, 5, 126, 5, 127, 5), Map.of(1981, 1, 1982, 1), 3000, 3500, Map.of());
        new Quest(46, false, Map.of(132, 3, 133, 3), Map.of(1983, 1, 1984, 1, 1985, 1), 0, 0, Map.of());
        new Quest(47, false, Map.of(), Map.of(1905, 1, 1906, 1, 1907, 1, 1908, 1, 1909, 1), 0, 0, Map.of(1374, 1, 1381, 1, 1383, 1, 1406, 1, 1411, 1));
        new Quest(48, false, Map.of(153, 3, 154, 5), Map.of(1910, 1, 1911, 1, 1912, 1, 1913, 1, 1914, 1), 0, 0, Map.of(455, 1));
        new Quest(49, true, Map.of(170, 5, 171, 5, 172, 5), Map.of(1986, 1, 1987, 1), 3500, 4000, Map.of());
        new Quest(50, false, Map.of(), Map.of(1988, 1), 0, 0, Map.of());
        new Quest(51, false, Map.of(), Map.of(1915, 1, 1916, 1, 1917, 1, 1918, 1, 1919, 1), 0, 0, Map.of(466, 1, 467, 1));
        new Quest(52, false, Map.of(191, 3, 192, 3, 193, 2), Map.of(1920, 1, 1921, 1, 1922, 1, 1923, 1, 1924, 1), 2000, -1, Map.of(1390, 2, 1400, 2));
        new Quest(53, true, Map.of(195, 3, 196, 5, 197, 2), Map.of(1989, 1, 1990, 1, 1991, 1), 4500, 5000, Map.of());
        new Quest(54, true, Map.of(200, 5, 201, 5), Map.of(1992, 1), 4000, 4500, Map.of());
        new Quest(55, false, Map.of(), Map.of(1925, 1, 1926, 1, 1927, 1, 1928, 1, 1929, 1), 0, 0, Map.of(476, 1, 477, 1));
        new Quest(56, false, Map.of(), Map.of(1993, 1, 1994, 1), 0, 0, Map.of());
        new Quest(57, false, Map.of(), Map.of(1995, 1, 1996, 1), 0, 0, Map.of(481, 1, 482, 1));
        new Quest(58, false, Map.of(), Map.of(1930, 1, 1931, 1, 1932, 1, 1933, 1, 1934, 1), 0, 0, Map.of(484, 1, 485, 1));
        new Quest(59, false, Map.of(), Map.of(1935, 1, 1936, 1, 1937, 1, 1938, 1, 1939, 1), 0, 0, Map.of(34, 1, 35, 1, 41, 1, 43, 1, 50, 1));
        new Quest(60, false, Map.of(), Map.of(1997, 1), 0, 0, Map.of());
        new Quest(61, true, Map.of(250, 5, 251, 5), Map.of(1998, 1), 5000, 5500, Map.of());
        new Quest(62, false, Map.of(), Map.of(1999, 1), 0, 0, Map.of());
        new Quest(63, false, Map.of(262, 3, 263, 3), Map.of(1940, 1, 1941, 1, 1942, 1, 1943, 1, 1944, 1), 0, 0, Map.of(38, 1, 39, 1, 45, 1, 52, 1, 54, 1));
        new Quest(64, false, Map.of(), Map.of(1945, 1, 1946, 1, 1947, 1, 1948, 1, 1949, 1), 0, 0, Map.of(498, 1));
    }    
}
