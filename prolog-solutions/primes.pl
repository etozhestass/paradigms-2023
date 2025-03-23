put_min_div(J, N) :- is_min_div(J, _), !.
put_min_div(J, N) :- assertz(is_min_div(J, N)).
build_help(J, N, I) :- J > N, !, I1 is I + 1, build(I1, N).
build_help(J, N, I) :- assertz(is_not_prime(J)), put_min_div(J, I), J1 is J + I, build_help(J1, N, I).
build(I, N) :- I * I > N, !.
build(I, N) :- is_not_prime(I), !, I1 is I + 1, build(I1, N).
build(I, N) :- II is I * I, build_help(II, N, I).
init(N_MAX) :- assertz(is_not_prime(0)), assertz(is_not_prime(1)), build(2, N_MAX).
prime(N) :- N > 1, \+ is_not_prime(N).
composite(N) :- N > 1, is_not_prime(N).
sorted_prime([]).
sorted_prime([A]) :- prime(A).
sorted_prime([A1, A2 | T]) :- prime(A1), A1 =< A2, List = [A2 | T], sorted_prime(List).
is_mul([], 1).
is_mul([H | T], R) :- is_mul(T, R1), R is R1 * H.
is_min_div(P, P) :- prime(P), !.
prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :- number(N), !, is_min_div(N, H), N1 is div(N, H), prime_divisors(N1, T).
prime_divisors(N, Divisiors) :- sorted_prime(Divisiors), is_mul(Divisiors, N).

pow(_, 0, 1) :- !.
pow(A, N, Ans) :- 0 is mod(N, 2), !, N1 is div(N, 2), pow(A, N1, Ans1), Ans is Ans1 * Ans1.
pow(A, N, Ans) :- N1 is N - 1, pow(A, N1, Ans1), Ans is Ans1 * A.

compact([], 0, []) :- !.
compact([P], Cnt, [(P, Cnt)]) :- !.
compact([P, P | T], Cnt, Ans) :- !, List = [P | T], Cnt1 is Cnt + 1, compact(List, Cnt1, Ans).
compact([P | T], Cnt, [(P, Cnt) | TR]) :- compact(T, 1, TR).

compact_prime_divisors(1, []) :- !.
compact_prime_divisors(N, CDs) :- number(N), !, prime_divisors(N, Divisors), compact(Divisors, 1, CDs).
compact_prime_divisors(N, [(P, Deg) | T]) :- compact_prime_divisors(N1, T), prime(P),
                                             pow(P, Deg, PDeg), N is N1 * PDeg, is_min_div(N, P).