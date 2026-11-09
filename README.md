# Assignment 4: Patient Zero

**Student ID:** 254000  
**Name:** Serzhan Kaldar  
**Group:** SE-2511

This repository contains my solution and report for Assignment 4. See `START_HERE.md` for setup instructions and `docs/Assignment4.pdf` for the assignment specification.

## Program output

The following results were printed by `java -cp out Main` using my data:

```text
Mutations: 110
Shared core: 1040
Marker hits: 5
First hit: 153
Border length: 4
Shared words: 323
Tests needed: 43
Cheapest testing: 172
Longest chain: 34
```

My outbreak report is available in `output/outbreak.html`. Screenshot:

![Outbreak report](output/outbreak.png)

## Questions

### Mission 1 — Edit distance

The cell `d[i][j]` stores the minimum number of edits needed to transform the first `i` characters of strain A into the first `j` characters of strain B. If the last characters are equal, the value comes from `d[i-1][j-1]`; otherwise, I take the minimum of deletion, insertion, and replacement (`d[i-1][j]`, `d[i][j-1]`, and `d[i-1][j-1]`) and add 1 for the edit. The first row and column are initialized with `0, 1, 2, ...` because an empty string needs that many insertions or deletions to reach the corresponding prefix.

### Mission 2 — Longest common subsequence

A subsequence keeps the original order of characters but does not require them to be next to each other, while a substring must be continuous. Therefore, the LCS can include matching letters separated by other characters, which is why `Shared core` may be larger than the number of letters matching at exactly the same positions in A and B.

### Mission 3 — Prefix function and KMP

The prefix function `pi[i]` gives the length of the longest proper prefix of the marker that is also a suffix of the marker prefix ending at position `i`. In KMP, when a mismatch occurs, the text pointer stays where it is and the marker position `k` is shortened using the prefix function, so already-checked text characters are not checked again. For my marker `CAGGGGCAGG`, the Border length is **4**: `CAGG` is both a proper prefix and a suffix.

### Mission 4 — Rabin–Karp

Equal hashes do not guarantee equal strings because different strings can produce the same hash (a collision), so I compare the actual characters whenever the hashes match. If many windows have equal hashes, these extra character comparisons make the algorithm slower; in the worst case, its time can reach `O(n·k)`.

### Mission 5 — Tree DP

For each person, I calculate two states: the minimum cost if that person is tested and the minimum cost if that person is not tested. If a person is not tested, every child must be tested; if the person is tested, each child can be either tested or not tested, whichever is cheaper. A greedy choice based only on the number of links can fail because it ignores testing costs and the choices required by neighboring people.

## AI use

I used **Claude (Anthropic)** as a helper during this assignment. I asked it to explain the algorithms (edit distance, LCS, prefix function and KMP, Rabin–Karp, and tree DP), help with code for Missions 1–5 and review it, explain compile errors (including a missing closing brace in `Marker.java`), and clarify Git commands such as `commit`, `push`, and `.gitignore`. I entered the code into the project, ran it, and checked the output with the lab console and `tools/Check.java`. The README explanations are written in my own words. I did not copy code or text from books or websites.

## Timing (Mission 7)

Times are in milliseconds, averaged over three runs of `java -cp out Bench`.

| Test | Size | Time (ms) |
|---|---:|---:|
| Naive search | 20,000 / 40,000 / 80,000 | 31.6 / 72.3 / 148.7 |
| KMP | 20,000 / 40,000 / 80,000 | 2.1 / 0.9 / 0.4 |
| Edit distance | 500 / 1,000 / 2,000 / 4,000 | 5.2 / 4.9 / 14.2 / 57.5 |

The naive search takes roughly twice as long when the text size doubles, which is consistent with linear growth for these inputs. KMP is also linear (`O(n + m)`), but its very small measurements fluctuate because of Java JIT compilation and measurement overhead. Edit distance grows much faster: when both input lengths double, the number of table cells grows about four times, matching its `O(n·m)` complexity.

## Complexity

Here, `n` and `m` are the lengths of the two strains or the text and marker, `k` is the window length, and `v` is the number of people.

| Algorithm | Time | Space | Assumptions |
|---|---|---|---|
| Edit distance | `O(n·m)` | `O(n·m)` | Full DP table; `n` and `m` are strain lengths |
| LCS | `O(n·m)` | `O(n·m)` | Full DP table; `n` and `m` are strain lengths |
| Prefix function and KMP | `O(n + m)` | `O(m)` for the prefix array; `O(n)` if all hit positions are stored | `n` is text length and `m` is marker length |
| Rabin–Karp (shared words) | `O(n + m)` average; `O(n·k)` worst case | `O(m)` | Average assumes few hash collisions; worst case has many equal hashes and character checks |
| Tree DP | `O(v)` | `O(v)` | Tree rooted at person 0; iterative DFS |
