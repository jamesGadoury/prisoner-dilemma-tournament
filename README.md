# prisoner-dilemma-tournament

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

Notes from Chat GPT on : "What was the probability that Axelrod used to determine if the game should end?"

In Robert Axelrod's iterated prisoner's dilemma tournaments, the length of each match (the number of rounds played between two strategies) was not fixed; instead, it was probabilistically determined to prevent participants from tailoring their strategies based on a known end point. To achieve this, Axelrod introduced a mechanism where the game had a certain probability of ending after each round.

Axelrod used a continuation probability, often denoted as ww, with a typical value of w=0.996w=0.996. This means that after each round, there was a 99.6% chance that the game would continue and a 0.4% chance (1 - 0.996) that it would end. This approach simulated an indefinite horizon for the game, preventing strategies from exploiting a known final round by, for example, defecting at the very end to gain a last-moment advantage.

The expected number of rounds in such a setup can be calculated using the formula for the expected value of a geometric distribution, which, in this context, is 11−w1−w1​. With w=0.996w=0.996, the expected length of a match would be approximately 250 rounds (10.004≈2500.0041​≈250), though the actual length could vary significantly due to the probabilistic nature of the termination condition.

This probabilistic ending enforced strategies to be robust and perform well over both short and long interactions, contributing to Axelrod's analysis of the evolution of cooperative behavior in the iterated prisoner's dilemma.