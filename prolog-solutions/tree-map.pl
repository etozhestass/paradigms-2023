node(K, V, L, R, H).

max(A, B, R) :- (A < B -> R = B ; R = A).

empty_node(node(null, null, null, null, 0)).
create_node(K, V, node(K, V, E, E, 1)) :- empty_node(E).
build_node(K, V, L, R, node(K, V, L, R, H)) :- height(L, Hl), height(R, Hr), max(Hl, Hr, H1), H is H1 + 1.
is_empty(Node) :- key(Node, null).

key   (node(K, _, _, _, _), K).
value (node(_, V, _, _, _), V).
left  (node(_, _, L, _, _), L).
right (node(_, _, _, R, _), R).
height(node(_, _, _, _, H), H).

bfactor(node(_, _, L, R, _), Ans) :- height(R, Hr), height(L, Hl), Ans is Hr - Hl.

rotate_right(node(K, V, node(Kl, Vl, Ll, Rl, _), R, _), Ans) :- build_node(K, V, Rl, R, Tmp), build_node(Kl, Vl, Ll, Tmp, Ans).

rotate_left (node(K, V, L, node(Kr, Vr, Lr, Rr, _), _), Ans) :- build_node(K, V, L, Lr, Tmp), build_node(Kr, Vr, Tmp, Rr, Ans).

balance(node(K, V, L, R, H), Ans) :- Node = node(K, V, L, R, H), bfactor(Node, 2), !, bfactor(R, Bfr),
											(Bfr < 0 -> rotate_right(R, FixedR), build_node(K, V, L, FixedR, Ans) ; rotate_left(Node, Ans)).
balance(node(K, V, L, R, H), Ans) :- Node = node(K, V, L, R, H), bfactor(Node, -2), !, bfactor(L, Bfl),
											(Bfl > 0 -> rotate_left(L, FixedL), build_node(K, V, FixedL, R, Ans) ; rotate_right(Node, Ans)).
balance(Ans, Ans).

map_put(E, Key, Value, Ans) :- is_empty(E), !, create_node(Key, Value, Ans).
map_put(node(K, _, L, R, _), Key, Value, Ans) :- K = Key, !, build_node(K, Value, L, R, Ans).
map_put(node(K, V, E, R, _), Key, Value, Ans) :- is_empty(E), K > Key, !, create_node(Key, Value, Ins), build_node(K, V, Ins, R, Ans).
map_put(node(K, V, L, E, _), Key, Value, Ans) :- is_empty(E), K < Key, !, create_node(Key, Value, Ins), build_node(K, V, L, Ins, Ans).
map_put(node(K, V, L, R, _), Key, Value, Ans) :- K > Key, !, map_put(L, Key, Value, NewL), build_node(K, V, NewL, R, Tmp), balance(Tmp, Ans).
map_put(node(K, V, L, R, _), Key, Value, Ans) :- map_put(R, Key, Value, NewR), build_node(K, V, L, NewR, Tmp), balance(Tmp, Ans).

map_get(E, Key, Value) :- is_empty(E), !, \+ true.
map_get(Node, Key, Value) :- key(Node, Key), value(Node, Value), !.
map_get(Node, Key, Value) :- key(Node, K), Key < K, !, left(Node, L), map_get(L, Key, Value).
map_get(Node, Key, Value) :- right(Node, R), map_get(R, Key, Value).

find_min(Node, Key, Value) :- left(Node, L), is_empty(L), !, key(Node, Key), value(Node, Value).
find_min(Node, Key, Value) :- left(Node, L), find_min(L, Key, Value).

remove_min(Node, Ans) :- left(Node, L), is_empty(L), !, right(Node, Ans).
remove_min(node(K, V, L, R, _), Ans) :- remove_min(L, NewL), build_node(K, V, NewL, R, Tmp), balance(Tmp, Ans).

map_remove(E, Key, E) :- is_empty(E), !.
map_remove(node(K, V, L, R, _), Key, Res) :- K > Key, !, map_remove(L, Key, NewL), build_node(K, V, NewL, R, Tmp), balance(Tmp, Res).
map_remove(node(K, V, L, R, _), Key, Res) :- K < Key, !, map_remove(R, Key, NewR), build_node(K, V, L, NewR, Tmp), balance(Tmp, Res).
map_remove(node(K, _, L, R, _), K, L) :- is_empty(R), !.
map_remove(node(K, _, L, R, _), K, Res) :- find_min(R, MinK, MinV), remove_min(R, NewR), build_node(MinK, MinV, L, NewR, Tmp), balance(Tmp, Res).

map_build([], E) :- empty_node(E).
map_build([(K, V) | T], Ans) :- map_build(T, Tmp), map_put(Tmp, K, V, Ans).

map_putIfAbsent(Node, Key, Value, Node) :- map_get(Node, Key, _), !.
map_putIfAbsent(Node, Key, Value, Res) :- map_put(Node, Key, Value, Res).