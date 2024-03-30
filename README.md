# prisoner-dilemma-tournament

## To run

```
./run-build-sim

docker compose up
```

## Todos
- Move kotlin logic into its own subdir + build
- Add new kotlin build logic to move main tournament function into a server that frontend can talk to
- Make frontend output information about all rounds for each game
- Make frontend attractive
- Implement more agents
- Implement interface to allow new user programs to be added as agents?
- Make agents added to tournament configurable through frontend?

## Random notes

Project inspired by Robert Axelrod's tournament (below generated by Chat GPT):

Robert Axelrod's tournaments, held in the early 1980s, were designed to find effective strategies in an iterated prisoner's dilemma context. Participants were invited to submit computer programs that would compete against each other, playing repeated rounds of the prisoner's dilemma. The rules of Axelrod's tournament were straightforward but pivotal in understanding cooperation's emergence and sustainability among rational players. Here are the key rules and setup of Axelrod's tournaments:

1. **Repeated Interactions**: Each pair of strategies competed in a series of rounds, playing the iterated prisoner's dilemma against each other. This iteration allowed strategies to potentially retaliate or cooperate based on the opponent's previous moves, unlike the single-shot prisoner's dilemma.

2. **Scoring System**: In each round, the payoffs were awarded based on the choices made by the two competing strategies. The typical payoff matrix was: 
   - Both cooperate: 3 points each
   - Both defect: 1 point each
   - One cooperates and the other defects: The defector gets 5 points, and the cooperator gets 0.

3. **Unknown Game Length**: The number of rounds in each matchup was not fixed but determined randomly, with a certain probability of the game ending after each round. This prevented strategies from exploiting a known end point (e.g., defecting on the last move).

4. **All-vs-All Format**: Each submitted strategy competed against all others, including itself and a random strategy. This comprehensive competition ensured that successful strategies needed to perform well across a diverse set of opponents.

5. **No Iteration Between Matches**: While strategies could remember and react to previous moves within a single match, they could not carry over information between matches against different opponents.

6. **Total Score**: The overall score for each strategy was the sum of points obtained in all its pairwise encounters. The strategy with the highest total score at the end of the tournament was considered the most successful.

7. **Anonymity of Entries**: Participants did not know the other entrants or their strategies during the competition, ensuring unbiased interactions.

The winning strategy of the first tournament was "Tit for Tat," submitted by Anatol Rapoport, which simply cooperated on the first move and then mimicked the opponent's previous move in subsequent rounds. The success of "Tit for Tat" highlighted the effectiveness of strategies that were nice (never the first to defect), retaliatory (responding to defection), forgiving (able to return to cooperation), and clear (easy to understand and predict).

Axelrod's tournaments had a profound impact on the study of cooperation, game theory, and the evolution of strategies in social science and biology, demonstrating that simple cooperative rules could thrive in a competitive environment.

---

Prompt : "What was the probability that Axelrod used to determine if the game should end?"

Chat GPT :

In Robert Axelrod's iterated prisoner's dilemma tournaments, the length of each match (the number of rounds played between two strategies) was not fixed; instead, it was probabilistically determined to prevent participants from tailoring their strategies based on a known end point. To achieve this, Axelrod introduced a mechanism where the game had a certain probability of ending after each round.

Axelrod used a continuation probability, often denoted as ww, with a typical value of w=0.996w=0.996. This means that after each round, there was a 99.6% chance that the game would continue and a 0.4% chance (1 - 0.996) that it would end. This approach simulated an indefinite horizon for the game, preventing strategies from exploiting a known final round by, for example, defecting at the very end to gain a last-moment advantage.

The expected number of rounds in such a setup can be calculated using the formula for the expected value of a geometric distribution, which, in this context, is 11−w1−w1​. With w=0.996w=0.996, the expected length of a match would be approximately 250 rounds (10.004≈2500.0041​≈250), though the actual length could vary significantly due to the probabilistic nature of the termination condition.

This probabilistic ending enforced strategies to be robust and perform well over both short and long interactions, contributing to Axelrod's analysis of the evolution of cooperative behavior in the iterated prisoner's dilemma.

---

Prompt : "Can you explain all the known strategies for Axelrod's tournament?"

Chat GPT :

Robert Axelrod's tournaments, which were pivotal in understanding cooperation in the iterated prisoner's dilemma, attracted diverse strategies from participants worldwide. While there are numerous strategies that could be discussed, here are some of the most well-known and influential ones that emerged from Axelrod's tournaments:

1. **Tit for Tat (TFT)**: Introduced by Anatol Rapoport, Tit for Tat starts by cooperating on the first move and then simply mimics the opponent's previous move. It's a strategy characterized by being nice (never defects first), retaliatory (responds to defection with defection), forgiving (returns to cooperation if the opponent does), and clear (easy for opponents to understand and predict).

2. **Always Defect (ALLD)**: This strategy defects on every move, irrespective of the opponent's actions. While it can exploit more cooperative strategies, it fails to establish mutual cooperation and may fare poorly against equally non-cooperative strategies.

3. **Always Cooperate (ALLC)**: This strategy cooperates in every round. While it's extremely nice, it's exploitable by more aggressive strategies and often ends up with lower payoffs when matched against them.

4. **Tit for Two Tats (TF2T)**: Similar to Tit for Tat, but more forgiving. It cooperates unless the opponent defects twice in a row. This approach can reduce the likelihood of endless retaliation cycles but is more susceptible to exploitation by defecting strategies.

5. **Suspicious Tit for Tat (STFT)**: This is a variation of Tit for Tat that starts by defecting in the first round instead of cooperating. It then follows the Tit for Tat mechanism for the rest of the game. It's slightly less nice than TFT, given its initial defection.

6. **Pavlov (Win-Stay, Lose-Shift)**: This strategy repeats the last choice if it was successful (received a high payoff) and changes if it was not. This means it cooperates after mutual cooperation or being exploited (when it defects), and defects after mutual defection or successfully exploiting the opponent.

7. **Random**: This strategy makes its move randomly, with no regard for the historical behavior of its opponent. It's unpredictable and can neither be exploited nor reliably exploit others.

8. **Gradual**: This strategy starts by cooperating and then responds to defections with a series of retaliatory defections but gradually increases the number of retaliatory defections with each subsequent provocation. It's designed to be forgiving and also to avoid being too exploitable.

9. **Contrite Tit for Tat**: An extension of Tit for Tat, this strategy takes into consideration the context of the game, particularly the possibility of noise (misinterpreted moves). It tries to correct for unintended defections due to noise, aiming to maintain cooperation.

The success of these strategies in the tournament varied, with context and pairing affecting outcomes significantly. Tit for Tat's success was notable for its simplicity and robustness, effectively illustrating the power of reciprocity in promoting cooperative behavior. The diversity of strategies submitted to Axelrod's tournaments provided deep insights into the dynamics of cooperation and conflict, informing not just game theory but also fields like economics, politics, and biology.

---

Prompt : "Can you come up with some novel strategies for Axelrod's tournament that haven't already been discussed or written?"

Chat GPT :

Certainly! Creating novel strategies for Axelrod's iterated prisoner's dilemma tournament involves considering various aspects of gameplay, such as adaptation, history analysis, and randomness, among others. Here are some innovative strategies that build upon existing concepts but introduce unique twists:

1. **Forgiving Tit for Tat (FTFT)**: This strategy starts like Tit for Tat but introduces a forgiveness mechanism. After an opponent defects, FTFT has a probability (say, 10%) to cooperate instead of defecting, offering a chance to break cycles of mutual retaliation and explore the potential for returning to mutual cooperation.

2. **Adaptive Mirror**: This strategy initially mirrors the opponent's moves but adapts its responsiveness over time. If the opponent cooperates more than a threshold (e.g., 75% of the time), Adaptive Mirror becomes more cooperative to encourage mutual cooperation. Conversely, if the opponent defects more than a certain threshold, the strategy becomes stricter, adopting a Tit for Tat or even an Always Defect stance to avoid exploitation.

3. **Pattern Matcher**: This strategy analyzes the opponent's move patterns, looking for repetitions or regular sequences. If a pattern is detected (e.g., alternate cooperation and defection), Pattern Matcher anticipates and counters the opponent's moves to maximize its own score.

4. **Cooperative Until Betrayed (CUB)**: CUB remains highly cooperative and only defects in response to an opponent's defection. However, unlike Tit for Tat, once an opponent defects, CUB switches to Always Defect for a set number of rounds before giving the opponent another chance to cooperate.

5. **Random Tit for Tat (RTFT)**: This strategy adds an element of unpredictability to the classic Tit for Tat. With a small probability, RTFT will make the opposite move of what Tit for Tat would typically do. This randomness can help in exploring unforeseen benefits of unexpected cooperation or defection while mostly maintaining a reciprocative approach.

6. **Historical Average Strategy (HAS)**: HAS calculates the average cooperation rate of the opponent over all previous rounds. If the cooperation rate is above a certain threshold, HAS cooperates; otherwise, it defects. This strategy evaluates the opponent's general tendency rather than just the most recent action.

7. **Dynamic Threshold Strategy (DTS)**: DTS dynamically adjusts its cooperation threshold based on its current score relative to the opponent. If DTS is leading, it becomes more cooperative to maintain a friendly relationship. If trailing, it becomes more competitive, increasing its threshold for cooperation to avoid being exploited.

8. **Emotional Quotient Strategy (EQS)**: EQS assigns an "emotional" state to the opponent based on their actions (happy, neutral, angry), influenced by the outcomes of recent rounds. EQS adapts its strategy to try to "appease" an angry opponent or "reward" a happy one, aiming to foster mutual cooperation.

These strategies incorporate elements of adaptability, unpredictability, and strategic depth, making them potentially effective in the diverse landscape of Axelrod's tournament. Their effectiveness would, of course, need to be tested through simulation and analysis in various tournament settings.