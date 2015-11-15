package twitterclustering;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * ListArithmetic class implements methods to add and subtract large integers which is otherwise not possible
 * by traditional data types
 */

public class ListArithmetic {

	/**
	 * ListArithmetic add method adds two linked lists and returns the result
	 */
	
	public static LinkedList<Integer> add(LinkedList<Integer> a,
			LinkedList<Integer> b) {

		boolean carryFlag = false;  // a flag to keep track of carry overs while adding
		LinkedList<Integer> res = new LinkedList<>();// store result

		if (a.isEmpty() && b.isEmpty()) { // empty input
			return null;
		} else {
			while (!a.isEmpty() && !b.isEmpty()) { // traversing from right to left 

				int sum;
				if (carryFlag) {
					sum = 1; // add carry of 1 since we are adding two numbers and carry never exceeds 1
				} else {
					sum = 0;
				}

				if ((a.getLast() + b.getLast()) / 10 == 1) // carry checking
				{
					carryFlag = true;

				} else {
					carryFlag = false;
				}

				if (carryFlag) {

					sum += (a.getLast() + b.getLast()) % 10; // add lsb of sum
																// to
																// result // no
																// chance of
																// overflow here
																// as
																// sum would not
																// exceed 18

				} else {
					sum += (a.getLast() + b.getLast());

				}
				res.addFirst(sum); // add sum to result

				// remove last from both the lists

				a.removeLast();
				b.removeLast();
			}

		}

		// find which is exhausted

		// list A is exhausted

	
		while (!b.isEmpty()) { // exhaust list b

			int sum; // local sum
			// check if there was a carry in previously
			if (carryFlag) {
				sum = 1;
			} else {
				sum = 0;
			}

			if ((sum + b.getLast()) / 10 == 1) // carry
			{
				carryFlag = true;

			} else {
				carryFlag = false;
			}

			if (carryFlag) {

				sum += b.getLast();

				sum = sum % 10; // add lsb of sum to result // no // chance of
								// overflow here as

			} else {
				sum += (b.getLast());

			}
			res.addFirst(sum); // add sum to result

			// remove last from both the lists
			b.removeLast();

		}

		// list B is exhausted

		

		while (!a.isEmpty()) {

			int sum;
			// check if there was a carry in previous step
			if (carryFlag) {
				sum = 1;
			} else {
				sum = 0;
			}

			if ((sum + a.getLast()) / 10 == 1) // carry
			{
				carryFlag = true;

			} else {
				carryFlag = false;
			}

			if (carryFlag) {

				sum += a.getLast();

				sum = sum % 10; // add lsb of sum to result // no // chance of
								// overflow here as

			} else {
				sum += (a.getLast());

			}
			res.addFirst(sum); // add sum to result

			// remove last from both the lists
			a.removeLast();

		}
		// a exhausted but still carry was remaining to be added in msb
		// add carry to msb
		if (carryFlag)
			res.addFirst(1);

		return res;

	}

	/**
	 * ListArithmetic convertStringToList method converts a String to a linkedlist
	 * @return LinkedList of integers
	 */
	
	public static LinkedList<Integer> convertStringToList(String num) {
		int result = 0;
		int zeroAscii = 48;
		int nineAscii = 57;
		LinkedList<Integer> res = new LinkedList<Integer>();

		for (char c : num.toCharArray()) {

			if (c >= zeroAscii && c <= nineAscii) {
				res.add(result * 10 + (c - zeroAscii));
			} else
				return null;
		}
		return res;
	}
	

	/**
	 * ListArithmetic sub method subtracts two linked lists and returns the result
	 */
	

	public static LinkedList<Integer> sub(LinkedList<Integer> a,
			LinkedList<Integer> b) {

		boolean borrowFlag = false;
		boolean prevBorrowFlag = false;
		// find the largest of two
		boolean flagA = false;
		boolean flagB = false;

		if (a.size() > b.size()) {
			flagA = true;
		} else if (a.size() < b.size()) {
			flagB = true;
		} else {
			// check msb of both
			int len = 0;

			while (len > a.size()) {
				if (a.get(len) > b.get(len)) {
					flagA = true;
					break;
				} else if (a.get(len) < b.get(len)) {
					flagB = true;
					break;
				} else {
					len++;
				}
			}

		}

		LinkedList<Integer> res = new LinkedList<>();

		// subtract B from A

		if (a.isEmpty() && b.isEmpty()) {
			return null;
		} else {

			while (!a.isEmpty() && !b.isEmpty()) {

				if (a.getLast() < b.getLast()) {
					borrowFlag = true;
				} else {
					borrowFlag = false;
				}

				int diff;
				if (borrowFlag) {
					if (!prevBorrowFlag) {
						diff = (a.getLast() + 10) - b.getLast();
					} else {
						diff = (a.getLast() + 9) - b.getLast();
					}

					prevBorrowFlag = true;

				} else {

					if (prevBorrowFlag) {
						diff = ((a.getLast() - 1) - b.getLast());
					} else {
						diff = (a.getLast() - b.getLast());
					}

					prevBorrowFlag = false;
				}

				res.addFirst(diff); // add diff to result

				// remove last from both the lists

				a.removeLast();
				b.removeLast();
			}

		}

		// list B is exhausted

		// check if there was a carry in previous step

		while (!a.isEmpty()) {

			int diff = 0;
			if (borrowFlag) {

				if (a.getLast() == 0 && prevBorrowFlag) {
					diff = (a.getLast() + 9);
					prevBorrowFlag = false;
				} else {
					diff = ((a.getLast() - 1));
				}

				borrowFlag = false;

			} else {
				diff = (a.getLast());
			}

			res.addFirst(diff);
			a.removeLast();
		}

		return res;
	}
	

	/**
	 * ListArithmetic listToString method converts a list to a String
	 * @return String representing the number in the inpiut list
	 * 
	 */

	public static String listToString(LinkedList<Integer> a) {

		String res = "";
		Iterator itr = a.iterator();
		while (itr.hasNext()) {

			res += itr.next().toString();
		}

		return res;
	}

	public static void main(String args[]) {

		System.out.println(listToString(sub(
				convertStringToList("10010101010101101010101010101010"),
				convertStringToList("99"))));

		System.out.println(listToString(add(
				convertStringToList("10010101010101101010101010101010"),
				convertStringToList("99"))));
		System.out.println();

	}
}